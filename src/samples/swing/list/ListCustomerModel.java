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
package samples.swing.list;


/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.3 $ $Date: 2002/09/05 15:41:48 $
 * @created 05 September 2002
 */
public class ListCustomerModel {

    private String name;
    private String address;
    private int age;


    /**
     * Constructor for the ListCustomerModel object
     */
    public ListCustomerModel() { }


    /**
     * Constructor for the ListCustomerModel object
     *
     * @param inName TODO: Describe the Parameter
     * @param inAddress TODO: Describe the Parameter
     * @param inAge TODO: Describe the Parameter
     */
    public ListCustomerModel(String inName,
            String inAddress,
            int inAge) {
        name = inName;
        address = inAddress;
        age = inAge;
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
     * Gets the address
     *
     * @return The address value
     */
    public String getAddress() {
        return address;
    }


    /**
     * Gets the age
     *
     * @return The age value
     */
    public int getAge() {
        return age;
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
     * Sets the address
     *
     * @param inAddress The new address value
     */
    public void setAddress(String inAddress) {
        address = inAddress;
    }


    /**
     * Sets the age
     *
     * @param inAge The new age value
     */
    public void setAge(int inAge) {
        age = inAge;
    }
}

