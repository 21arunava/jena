/*
 * (c) Copyright 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.sparql.serializer;

import com.hp.hpl.jena.sparql.syntax.*;
import com.hp.hpl.jena.sparql.util.IndentedWriter;

/** com.hp.hpl.jena.query.core.FormatterXML
 * 
 * @author Andy Seaborne
 * @version $Id: FormatterXML.java,v 1.13 2007/01/31 13:01:20 andy_seaborne Exp $
 */

public class FormatterXML extends FormatterBase
    implements FormatterElement, FormatterTemplate 
{
    public FormatterXML(IndentedWriter out, SerializationContext context)
    {
        super(out, context) ;
    }
    
    public boolean topMustBeGroup() { return false ; }

    public void visit(ElementTriplesBlock el)  { }

    public void visit(ElementDataset el)            { }

    public void visit(ElementFilter el)             { }

    public void visit(ElementUnion el)              { }

    public void visit(ElementGroup el)              { }

    public void visit(ElementOptional el)           { }

    public void visit(ElementNamedGraph el)         { }

    public void visit(ElementUnsaid el)             { }
    
    public void visit(ElementExtension el)          { }

    public void visit(TemplateTriple template)      { }

    public void visit(TemplateGroup template)       { }

}

/*
 * (c) Copyright 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */