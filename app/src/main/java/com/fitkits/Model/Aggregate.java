package com.fitkits.Model;

import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class  Aggregate {

@SerializedName("aggregate")
@Expose
private Aggregate_ aggregate;

public Aggregate_ getAggregate() {
return aggregate;
}

public void setAggregate(Aggregate_ aggregate) {
this.aggregate = aggregate;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("aggregate", aggregate).toString();
}

}

