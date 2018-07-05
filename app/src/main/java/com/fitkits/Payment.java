
package com.fitkits;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Payment {

@SerializedName(value = "_id", alternate = "id")
@Expose
private String id;
@SerializedName("entity")
@Expose
private String entity;
@SerializedName("amount")
@Expose
private Integer amount;
@SerializedName("currency")
@Expose
private String currency;
@SerializedName("status")
@Expose
private String status;
@SerializedName("order_id")
@Expose
private Object orderId;
@SerializedName("invoice_id")
@Expose
private Object invoiceId;
@SerializedName("international")
@Expose
private Boolean international;
@SerializedName("method")
@Expose
private String method;
@SerializedName("amount_refunded")
@Expose
private Integer amountRefunded;
@SerializedName("refund_status")
@Expose
private Object refundStatus;
@SerializedName("captured")
@Expose
private Boolean captured;
@SerializedName("description")
@Expose
private String description;
@SerializedName("card_id")
@Expose
private Object cardId;
@SerializedName("bank")
@Expose
private String bank;
@SerializedName("wallet")
@Expose
private Object wallet;
@SerializedName("vpa")
@Expose
private Object vpa;
@SerializedName("email")
@Expose
private String email;
@SerializedName("contact")
@Expose
private String contact;
@SerializedName("notes")
@Expose
private List<Object> notes = null;
@SerializedName("fee")
@Expose
private Integer fee;
@SerializedName("tax")
@Expose
private Integer tax;
@SerializedName("error_code")
@Expose
private Object errorCode;
@SerializedName("error_description")
@Expose
private Object errorDescription;
@SerializedName("created_at")
@Expose
private Integer createdAt;

public Payment(String id,int amount){
  this.id=id;
  this.amount=amount;

}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getEntity() {
return entity;
}

public void setEntity(String entity) {
this.entity = entity;
}

public Integer getAmount() {
return amount;
}

public void setAmount(Integer amount) {
this.amount = amount;
}

public String getCurrency() {
return currency;
}

public void setCurrency(String currency) {
this.currency = currency;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public Object getOrderId() {
return orderId;
}

public void setOrderId(Object orderId) {
this.orderId = orderId;
}

public Object getInvoiceId() {
return invoiceId;
}

public void setInvoiceId(Object invoiceId) {
this.invoiceId = invoiceId;
}

public Boolean getInternational() {
return international;
}

public void setInternational(Boolean international) {
this.international = international;
}

public String getMethod() {
return method;
}

public void setMethod(String method) {
this.method = method;
}

public Integer getAmountRefunded() {
return amountRefunded;
}

public void setAmountRefunded(Integer amountRefunded) {
this.amountRefunded = amountRefunded;
}

public Object getRefundStatus() {
return refundStatus;
}

public void setRefundStatus(Object refundStatus) {
this.refundStatus = refundStatus;
}

public Boolean getCaptured() {
return captured;
}

public void setCaptured(Boolean captured) {
this.captured = captured;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public Object getCardId() {
return cardId;
}

public void setCardId(Object cardId) {
this.cardId = cardId;
}

public String getBank() {
return bank;
}

public void setBank(String bank) {
this.bank = bank;
}

public Object getWallet() {
return wallet;
}

public void setWallet(Object wallet) {
this.wallet = wallet;
}

public Object getVpa() {
return vpa;
}

public void setVpa(Object vpa) {
this.vpa = vpa;
}

public String getEmail() {
return email;
}

public void setEmail(String email) {
this.email = email;
}

public String getContact() {
return contact;
}

public void setContact(String contact) {
this.contact = contact;
}

public List<Object> getNotes() {
return notes;
}

public void setNotes(List<Object> notes) {
this.notes = notes;
}

public Integer getFee() {
return fee;
}

public void setFee(Integer fee) {
this.fee = fee;
}

public Integer getTax() {
return tax;
}

public void setTax(Integer tax) {
this.tax = tax;
}

public Object getErrorCode() {
return errorCode;
}

public void setErrorCode(Object errorCode) {
this.errorCode = errorCode;
}

public Object getErrorDescription() {
return errorDescription;
}

public void setErrorDescription(Object errorDescription) {
this.errorDescription = errorDescription;
}

public Integer getCreatedAt() {
return createdAt;
}

public void setCreatedAt(Integer createdAt) {
this.createdAt = createdAt;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("id", id).append("entity", entity).append("amount", amount).append("currency", currency).append("status", status).append("orderId", orderId).append("invoiceId", invoiceId).append("international", international).append("method", method).append("amountRefunded", amountRefunded).append("refundStatus", refundStatus).append("captured", captured).append("description", description).append("cardId", cardId).append("bank", bank).append("wallet", wallet).append("vpa", vpa).append("email", email).append("contact", contact).append("notes", notes).append("fee", fee).append("tax", tax).append("errorCode", errorCode).append("errorDescription", errorDescription).append("createdAt", createdAt).toString();
}

}