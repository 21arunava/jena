/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.propertytable.lang;

import static org.apache.jena.riot.RDFLanguages.CSV;

import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.atlas.csv.CSVParser;
import org.apache.jena.atlas.web.ContentType;
import org.apache.jena.propertytable.util.IRILib;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RDFParserRegistry;
import org.apache.jena.riot.ReaderRIOT;
import org.apache.jena.riot.ReaderRIOTFactory;
import org.apache.jena.riot.lang.LangRIOT;
import org.apache.jena.riot.system.ErrorHandler;
import org.apache.jena.riot.system.ErrorHandlerFactory;
import org.apache.jena.riot.system.IRIResolver;
import org.apache.jena.riot.system.ParserProfile;
import org.apache.jena.riot.system.RiotLib;
import org.apache.jena.riot.system.StreamRDF;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.sparql.util.Context;

public class LangCSV implements LangRIOT {

	public static final String CSV_PREFIX = "http://w3c/future-csv-vocab/";
	public static final String CSV_ROW = CSV_PREFIX + "row";

	private InputStream input = null;
	private Reader reader = null;
	private String base;
	private String filename;
	private StreamRDF sink;
	private ParserProfile profile; // Warning - we don't use all of this.
	
	public static void register(){
		RDFParserRegistry.removeRegistration(Lang.CSV);
		RDFParserRegistry.registerLangTriples(Lang.CSV, new ReaderRIOTFactoryCSV());
	}

	@Override
	public Lang getLang() {
		return RDFLanguages.CSV;
	}

	@Override
	public ParserProfile getProfile() {
		return profile;
	}

	@Override
	public void setProfile(ParserProfile profile) {
		this.profile = profile;
	}

	public LangCSV(Reader reader, String base, String filename,
			ErrorHandler errorHandler, StreamRDF sink) {
		this.reader = reader;
		this.base = base;
		this.filename = filename;
		this.sink = sink;
		this.profile = RiotLib.profile(getLang(), base, errorHandler);
	}

	public LangCSV(InputStream in, String base, String filename,
			ErrorHandler errorHandler, StreamRDF sink) {
		this.input = in;
		this.base = base;
		this.filename = filename;
		this.sink = sink;
		this.profile = RiotLib.profile(getLang(), base, errorHandler);
	}

	@Override
	public void parse() {
		sink.start();
		CSVParser parser = (input != null) ? CSVParser.create(input)
				: CSVParser.create(reader);
		List<String> row = null;
		ArrayList<Node> predicates = new ArrayList<Node>();
		int rowNum = 0;
		while ((row = parser.parse1()) != null) {
			
			if (rowNum == 0) {
				for (String column : row) {
					String uri = IRIResolver.resolveString(filename) + "#"
							+ toSafeLocalname(column);
					Node predicate = this.profile.createURI(uri, rowNum, 0);
					predicates.add(predicate);
				}
			} else {
				//Node subject = this.profile.createBlankNode(null, -1, -1);
				Node subject = caculateSubject(rowNum, filename);
				Node predicateRow = this.profile.createURI(CSV_ROW, -1, -1);
				Node objectRow = this.profile
						.createTypedLiteral((rowNum + ""),
								XSDDatatype.XSDinteger, rowNum, 0);
				sink.triple(this.profile.createTriple(subject, predicateRow,
						objectRow, rowNum, 0));
				for (int col = 0; col < row.size() && col<predicates.size(); col++) {
					Node predicate = predicates.get(col);
					String columnValue = row.get(col).trim();
					if("".equals(columnValue)){
						continue;
					}					
					Node o;
					try {
						// Try for a double.
						double d = Double.parseDouble(columnValue);
						o = NodeFactory.createLiteral(columnValue,
								XSDDatatype.XSDdouble);
					} catch (Exception e) {
						o = NodeFactory.createLiteral(columnValue);
					}
					sink.triple(this.profile.createTriple(subject, predicate,
							o, rowNum, col));
				}

			}
			rowNum++;
		}
		sink.finish();

	}

	public static String toSafeLocalname(String raw) {
		String ret = raw.trim();
		return encodeURIComponent(ret);
		
	}
	
	public static String encodeURIComponent(String s) {
	    return IRILib.encodeUriComponent(s);
	}
	
	public static Node caculateSubject(int rowNum, String filename){
		Node subject = NodeFactory.createAnon();
//		String uri = IRIResolver.resolveString(filename) + "#Row_" + rowNum; 
//		Node subject =  NodeFactory.createURI(uri);
		return subject;
	}
	
	
	
	
    private static class ReaderRIOTFactoryCSV implements ReaderRIOTFactory
    {
        @Override
        public ReaderRIOT create(Lang lang) {
            return new ReaderRIOTLangCSV(lang) ;
        }
    }

    private static class ReaderRIOTLangCSV implements ReaderRIOT
    {
        private final Lang lang ;
        private ErrorHandler errorHandler ; 
        private ParserProfile parserProfile = null ;

        ReaderRIOTLangCSV(Lang lang) {
            this.lang = lang ;
            errorHandler = ErrorHandlerFactory.getDefaultErrorHandler() ;
        }

        @Override
        public void read(InputStream in, String baseURI, ContentType ct, StreamRDF output, Context context) {
            if ( lang == CSV){
            	LangRIOT parser = new LangCSV (in, baseURI, baseURI, ErrorHandlerFactory.getDefaultErrorHandler(),  output);
                if ( parserProfile != null )
                    parser.setProfile(parserProfile);
                if ( errorHandler != null )
                    parser.getProfile().setHandler(errorHandler) ;
                parser.parse() ;
            } else {
            	throw new IllegalArgumentException("The Lang must be 'CSV'!");
            }

        }

        @Override
        public void read(Reader in, String baseURI, ContentType ct, StreamRDF output, Context context) {
        	if ( lang == CSV){
        		LangRIOT parser = new LangCSV (in, baseURI, baseURI, ErrorHandlerFactory.getDefaultErrorHandler(),  output);
                if ( parserProfile != null )
                    parser.setProfile(parserProfile);
                if ( errorHandler != null )
                    parser.getProfile().setHandler(errorHandler) ;
        		parser.parse() ;
        	} else {
            	throw new IllegalArgumentException("The Lang must be 'CSV'!");
            }
        }

        @Override public ErrorHandler getErrorHandler()                     { return errorHandler ; }
        @Override public void setErrorHandler(ErrorHandler errorHandler)    { this.errorHandler = errorHandler ; }

        @Override public ParserProfile getParserProfile()                   { return parserProfile ; } 
        @Override public void setParserProfile(ParserProfile parserProfile) { this.parserProfile = parserProfile ; }
    }
}
