package com.fitkits.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Aggregate_ {

@SerializedName("value")
@Expose
private List<Value> value = null;

public List<Value> getValue() {
return value;
}

public void setValue(List<Value> value) {
this.value = value;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("value", value).toString();
}

}
