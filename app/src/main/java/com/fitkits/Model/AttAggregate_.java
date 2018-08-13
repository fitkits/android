package com.fitkits.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class AttAggregate_ {

@SerializedName("value")
@Expose
private List<Average> value = null;

public List<Average> getValue() {
return value;
}

public void setValue(List<Average> value) {
this.value = value;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("value", value).toString();
}

}
