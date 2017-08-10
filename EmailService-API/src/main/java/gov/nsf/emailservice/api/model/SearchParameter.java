package gov.nsf.emailservice.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.ALWAYS)
@Generated("org.jsonschema2pojo")
public class SearchParameter {
    private String id;
    private String key;
    private String value;

    public SearchParameter(){

    }
    public SearchParameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("id=" + id + ", ");
        sb.append("key=" + key +  ", ");
        sb.append("value=" + value + "}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj){

        if(obj == null) {
            return false;
        } else if(this.getClass() != obj.getClass()) {
            return false;
        } else {
            SearchParameter other = (SearchParameter) obj;
            return this.key.equals(other.key) && this.value.equals(other.value);
        }
    }
}
