/*
 * Scope: a generic MVC framework.
 * Copyright (c) 2000-2002, The Scope team
 * All rights reserved.
 *
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * Neither the name "Scope" nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *
 * $Id: pretty.settings,v 1.4 2002/09/19 18:10:27 ludovicc Exp $
 */
package samples.servlet.xml.validation;

import java.util.Date;
import java.util.List;

/**
 * <P>
 *
 * ValidationController uses one of these to supply data for its Views, and to
 * keep session state in. It contains a number of properties, including a simple
 * String, a Date that will involve a String conversion when the View
 * repopulates its model, and a property that has a validation rule that may
 * result in a thrown Exception. </P> <P>
 *
 * This model contains a list of String validation failures that are put in red
 * at the top of the page. </P> <P>
 *
 * Scope's default ModelManager implementation builds on the JavaBeans API, so
 * this model object is just a normal JavaBean. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.5 $ $Date: 2002/09/05 15:41:51 $
 * @created 05 September 2002
 */
public class ValidationModel {

    private String name;
    private Date date;
    private int number;
    private List validationErrors;

    /**
     * Constructor for the ValidationModel object
     */
    public ValidationModel() {
        setName("Fred");
        setDate(new Date(0));
    }

    /**
     * Gets the name
     *
     * @return The name value
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the date
     *
     * @return The date value
     */
    public Date getDate() {
        return date;
    }

    /**
     * Gets the number
     *
     * @return The number value
     */
    public int getNumber() {
        return number;
    }

    /**
     * Gets the validation failures
     *
     * @return The validationFailures value
     */
    public List getValidationFailures() {
        return validationErrors;
    }

    /**
     * Sets the name
     *
     * @param inName The new name value
     */
    public void setName(String inName) {
        name = inName;
    }

    /**
     * Sets the date
     *
     * @param inDate The new date value
     */
    public void setDate(Date inDate) {
        date = inDate;
    }

    /**
     * Sets the number
     *
     * @param inNumber The new number value
     * @throws Exception TODO: Describe the Exception
     */
    public void setNumber(int inNumber) throws Exception {
        if (inNumber > 10) {
            throw new IllegalArgumentException("Number must be less than 10.");
        }
        number = inNumber;
    }

    /**
     * Sets the validation failures
     *
     * @param inFailures The new validationFailures value
     */
    public void setValidationFailures(List inFailures) {
        validationErrors = inFailures;
    }
}
