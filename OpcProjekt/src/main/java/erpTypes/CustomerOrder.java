/*
 * Copyright (c) 2015 Tarek Auel
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package erpTypes;

import java.io.Serializable;

/**
 * @author Tarek Auel
 * @since 08.04.2015
 */
public class CustomerOrder implements Serializable {

    private final String customerFirstName;

    private final String customerLastName;

    private final String customerNumber;

    private final long orderNumber;

    private final OrderPosition[] positions;

    public CustomerOrder(String customerFirstName, String customerLastName, String customerNumber, long orderNumber, OrderPosition[] positions) {
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerNumber = customerNumber;
        this.orderNumber = orderNumber;
        this.positions = positions;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public long getOrderNumber() {
        return orderNumber;
    }

    public OrderPosition[] getPositions() {
        return positions;
    }
}
