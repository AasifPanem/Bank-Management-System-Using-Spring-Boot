package org.bank.entity;

import java.sql.Date;
import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Transaction {
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionid;
    private Date transactiondate;
    private String transactiontype;
    private String transactiontime;
    private double transactionamount;
    @Column(nullable = false)
    private Integer customerid;
}
