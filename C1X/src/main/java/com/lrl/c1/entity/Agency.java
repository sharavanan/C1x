/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrl.c1.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author logicresearch
 */
@Entity
@Table(name = "agency", catalog = "c2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Agency.findAll", query = "SELECT a FROM Agency a"),
    @NamedQuery(name = "Agency.findById", query = "SELECT a FROM Agency a WHERE a.id = :id"),
    @NamedQuery(name = "Agency.findByName", query = "SELECT a FROM Agency a WHERE a.name = :name"),
    @NamedQuery(name = "Agency.findByAddress", query = "SELECT a FROM Agency a WHERE a.address = :address"),
    @NamedQuery(name = "Agency.findByAddress2", query = "SELECT a FROM Agency a WHERE a.address2 = :address2"),
    @NamedQuery(name = "Agency.findByCity", query = "SELECT a FROM Agency a WHERE a.city = :city"),
    @NamedQuery(name = "Agency.findByState", query = "SELECT a FROM Agency a WHERE a.state = :state"),
    @NamedQuery(name = "Agency.findByZipCode", query = "SELECT a FROM Agency a WHERE a.zipCode = :zipCode"),
    @NamedQuery(name = "Agency.findByBillingContactFirstName", query = "SELECT a FROM Agency a WHERE a.billingContactFirstName = :billingContactFirstName"),
    @NamedQuery(name = "Agency.findByBillingContactLastName", query = "SELECT a FROM Agency a WHERE a.billingContactLastName = :billingContactLastName"),
    @NamedQuery(name = "Agency.findByBillingContactPhone", query = "SELECT a FROM Agency a WHERE a.billingContactPhone = :billingContactPhone"),
    @NamedQuery(name = "Agency.findByBillingContactEmail", query = "SELECT a FROM Agency a WHERE a.billingContactEmail = :billingContactEmail"),
    @NamedQuery(name = "Agency.findByPaymentMethod", query = "SELECT a FROM Agency a WHERE a.paymentMethod = :paymentMethod"),
    @NamedQuery(name = "Agency.findByPaymentDetails", query = "SELECT a FROM Agency a WHERE a.paymentDetails = :paymentDetails"),
    @NamedQuery(name = "Agency.findByIntegrationId", query = "SELECT a FROM Agency a WHERE a.integrationId = :integrationId"),
    @NamedQuery(name = "Agency.findByCreated", query = "SELECT a FROM Agency a WHERE a.created = :created"),
    @NamedQuery(name = "Agency.findByUpdated", query = "SELECT a FROM Agency a WHERE a.updated = :updated")})
public class Agency implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Size(max = 300)
    @Column(name = "Name")
    private String name;
    @Size(max = 300)
    @Column(name = "Address")
    private String address;
    @Size(max = 300)
    @Column(name = "Address2")
    private String address2;
    @Size(max = 90)
    @Column(name = "City")
    private String city;
    @Size(max = 90)
    @Column(name = "State")
    private String state;
    @Size(max = 90)
    @Column(name = "ZipCode")
    private String zipCode;
    @Size(max = 150)
    @Column(name = "BillingContactFirstName")
    private String billingContactFirstName;
    @Size(max = 150)
    @Column(name = "BillingContactLastName")
    private String billingContactLastName;
    @Size(max = 150)
    @Column(name = "BillingContactPhone")
    private String billingContactPhone;
    @Size(max = 300)
    @Column(name = "BillingContactEmail")
    private String billingContactEmail;
    @Size(max = 150)
    @Column(name = "PaymentMethod")
    private String paymentMethod;
    @Size(max = 300)
    @Column(name = "PaymentDetails")
    private String paymentDetails;
    @Size(max = 50)
    @Column(name = "IntegrationId")
    private String integrationId;
    @Column(name = "Created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Column(name = "Updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @JoinColumn(name = "UpdatedBy", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Userprofile updatedBy;
    @JoinColumn(name = "CreatedBy", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Userprofile createdBy;

    public Agency() {
    }

    public Agency(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getBillingContactFirstName() {
        return billingContactFirstName;
    }

    public void setBillingContactFirstName(String billingContactFirstName) {
        this.billingContactFirstName = billingContactFirstName;
    }

    public String getBillingContactLastName() {
        return billingContactLastName;
    }

    public void setBillingContactLastName(String billingContactLastName) {
        this.billingContactLastName = billingContactLastName;
    }

    public String getBillingContactPhone() {
        return billingContactPhone;
    }

    public void setBillingContactPhone(String billingContactPhone) {
        this.billingContactPhone = billingContactPhone;
    }

    public String getBillingContactEmail() {
        return billingContactEmail;
    }

    public void setBillingContactEmail(String billingContactEmail) {
        this.billingContactEmail = billingContactEmail;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Userprofile getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Userprofile updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Userprofile getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Userprofile createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Agency)) {
            return false;
        }
        Agency other = (Agency) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.lrl.c1.buyside.entity.Agency[ id=" + id + " ]";
    }
    
}
