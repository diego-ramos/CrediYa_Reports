package com.crediya.dynamodb.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.time.LocalDateTime;

/* Enhanced DynamoDB annotations are incompatible with Lombok #1932
         https://github.com/aws/aws-sdk-java-v2/issues/1932*/
@DynamoDbBean
public class TotalsEntity {

    private String totalKey;
    private String totalValue;
    private LocalDateTime updateDate;

    public TotalsEntity() {
    }

    public TotalsEntity(String totalKey, String totalValue, LocalDateTime updateDate) {
        this.totalKey = totalKey;
        this.totalValue = totalValue;
        this.updateDate = updateDate;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("totalKey")
    public String getTotalKey() {
        return totalKey;
    }

    public void setTotalKey(String totalKey) {
        this.totalKey = totalKey;
    }

    @DynamoDbAttribute("totalValue")
    public String getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }

    @DynamoDbAttribute("updateDate")
    public LocalDateTime getUpdateDate() {return updateDate;}

    public void setUpdateDate(LocalDateTime updateDate) {this.updateDate = updateDate;}
}
