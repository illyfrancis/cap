package com.acme.cap.generator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Transaction {
    @XmlAttribute
    private String transactionId;
    @XmlAttribute
    private String accountNumber;
    @XmlAttribute
    private String status;

    public Transaction() {
        // must have no arg default constructor for JAXB to work
    }

    private Transaction(String transactionId, String accountNumber, String status) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.status = status;
    }

    public static Transaction of(String transactionId, String accountNumber, String status) {
        return new Transaction(transactionId, accountNumber, status);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("tx", transactionId).add("acct", accountNumber)
                .add("status", status).toString();
    }

}