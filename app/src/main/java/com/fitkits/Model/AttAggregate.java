package com.fitkits.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class AttAggregate {

@SerializedName("aggregate")
@Expose
private AttAggregate_ aggregate;

public AttAggregate_ getAggregate() {
return aggregate;
}

public void setAggregate(AttAggregate_ aggregate) {
this.aggregate = aggregate;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("aggregate", aggregate).toString();
}

}

