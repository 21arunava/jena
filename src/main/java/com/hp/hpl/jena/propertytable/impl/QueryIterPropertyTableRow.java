package com.hp.hpl.jena.propertytable.impl;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.propertytable.PropertyTable;
import com.hp.hpl.jena.propertytable.Row;
import com.hp.hpl.jena.sparql.ARQInternalErrorException;
import com.hp.hpl.jena.sparql.core.BasicPattern;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.engine.ExecutionContext;
import com.hp.hpl.jena.sparql.engine.QueryIterator;
import com.hp.hpl.jena.sparql.engine.binding.Binding;
import com.hp.hpl.jena.sparql.engine.binding.BindingFactory;
import com.hp.hpl.jena.sparql.engine.binding.BindingMap;
import com.hp.hpl.jena.sparql.engine.iterator.QueryIter;
import com.hp.hpl.jena.sparql.engine.iterator.QueryIterRepeatApply;
import com.hp.hpl.jena.util.iterator.ClosableIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.NiceIterator;
import com.hp.hpl.jena.util.iterator.WrappedIterator;

public class QueryIterPropertyTableRow  extends QueryIterRepeatApply{
    private final BasicPattern pattern ;
    
    public QueryIterPropertyTableRow( QueryIterator input,
                                   BasicPattern pattern , 
                                   ExecutionContext cxt)
    {
        super(input, cxt) ;
        this.pattern = pattern ;
    }

    @Override
    protected QueryIterator nextStage(Binding binding)
    {
        return new RowMapper(binding, pattern, getExecContext()) ;
    }
    
    static int countMapper = 0 ; 
    static class RowMapper extends QueryIter
    {   
    	private PropertyTable table;
    	
    	private BasicPattern pattern;
        private Binding binding ;
        private ClosableIterator<Row> graphIter ;
        private Binding slot = null ;
        private boolean finished = false ;
        private volatile boolean cancelled = false ;

        RowMapper(Binding binding, BasicPattern pattern, ExecutionContext cxt)
        {
            super(cxt) ;
            GraphPropertyTable graph = (GraphPropertyTable)cxt.getActiveGraph() ;
            
            this.pattern = substitute(pattern, binding);
            this.binding = binding ;
            BasicPattern pattern2 = tripleNode(pattern);
            
            ExtendedIterator<Row> iter = graph.propertyTableBaseFind( new RowMatch( pattern2) );
            
            if ( false )
            {
                // Materialize the results now. Debugging only.
                List<Row> x = iter.toList() ;
                this.graphIter = WrappedIterator.create(x.iterator()) ;
                iter.close();
            }
            else
                // Stream.
                this.graphIter = iter ;
        }

        private static Node tripleNode(Node node)
        {
            if ( node.isVariable() )
                return Node.ANY ;
            return node ;
        }
        
        private static BasicPattern tripleNode(BasicPattern pattern)
        {
        	List<Triple> triples = new ArrayList<Triple>();
        	for (Triple triple: pattern){
        		triples.add( tripleNode(triple) );
        	}
        	return BasicPattern.wrap(triples);
        }
        
        private static Triple tripleNode(Triple triple){
            Node s = tripleNode(triple.getSubject()) ;
            Node p = tripleNode(triple.getPredicate()) ;
            Node o = tripleNode(triple.getObject()) ;
            return Triple.create(s, p, o);
        }

        private static Node substitute(Node node, Binding binding)
        {
            if ( Var.isVar(node) )
            {
                Node x = binding.get(Var.alloc(node)) ;
                if ( x != null )
                    return x ;
            }
            return node ;
        }
        
        private static Triple substitute(Triple triple, Binding binding){
            Node s = substitute(triple.getSubject(), binding) ;
            Node p = substitute(triple.getPredicate(), binding) ;
            Node o = substitute(triple.getObject(), binding) ;
            return Triple.create(s, p, o);
        }
        
        private static BasicPattern substitute(BasicPattern pattern , Binding binding)
        {
        	List<Triple> triples = new ArrayList<Triple>();
        	for (Triple triple: pattern){
        		triples.add( substitute(triple,binding) );
        	}
        	return BasicPattern.wrap(triples);
        }
        
        private Binding mapper(Row r)
        {
            BindingMap results = BindingFactory.create(binding) ;

            if ( ! insert(pattern, r, results) )
                return null ; 
            return results ;
        }
        
        private static boolean insert(BasicPattern input, Row output, BindingMap results)
        {	
        	for (Triple triple: input){
        		if (! insert(triple, output, results) ){
        			return false;
        		}
        	}
        	return true;
        }
        
        private static boolean insert(Triple input, Row output, BindingMap results){
        	if ( ! insert(input.getSubject(), output.getRowKey(), results) )
                return false ;
//        	if ( ! insert(input.getPredicate(), output.get, results) )
//                return false ;
            if ( ! insert(input.getObject(), output.getValue( input.getPredicate() ), results) )
                return false ;
            return true;
        }

        private static boolean insert(Node inputNode, Node outputNode, BindingMap results)
        {
            if ( ! Var.isVar(inputNode) )
                return true ;
            
            Var v = Var.alloc(inputNode) ;
            Node x = results.get(v) ;
            if ( x != null )
                return outputNode.equals(x) ;
            
            results.add(v, outputNode) ;
            return true ;
        }
        
        @Override
        protected boolean hasNextBinding()
        {
            if ( finished ) return false ;
            if ( slot != null ) return true ;
            if ( cancelled )
            {
                graphIter.close() ;
                finished = true ;
                return false ;
            }

            while(graphIter.hasNext() && slot == null )
            {
                Row r = graphIter.next() ;
                slot = mapper(r) ;
            }
            if ( slot == null )
                finished = true ;
            return slot != null ;
        }

        @Override
        protected Binding moveToNextBinding()
        {
            if ( ! hasNextBinding() ) 
                throw new ARQInternalErrorException() ;
            Binding r = slot ;
            slot = null ;
            return r ;
        }

        @Override
        protected void closeIterator()
        {
            if ( graphIter != null )
                NiceIterator.close(graphIter) ;
            graphIter = null ;
        }
        
        @Override
        protected void requestCancel()
        {
            // The QueryIteratorBase machinary will do the real work.
            cancelled = true ;
        }
    }
}
