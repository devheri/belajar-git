package com.whty.smartpos.unionpay.pay.msgmanager.model;

import java.util.ArrayList;
import java.util.List;

public class ParseElement {

    private String id;
    private String tag;
    private String len;
    private String value;
    private String lllvar;
    private String code;
    private String comments;
    private String targetClass;
    private String parseMethod;
    private String buildMethod;
    private String parseMethodParam;
    private String buildMethodParam;

    public String getParseMethodParam() {
        return parseMethodParam;
    }

    public void setParseMethodParam(String parseMethodParam) {
        this.parseMethodParam = parseMethodParam;
    }

    public String getBuildMethodParam() {
        return buildMethodParam;
    }

    public void setBuildMethodParam(String buildMethodParam) {
        this.buildMethodParam = buildMethodParam;
    }


    private List<ParseElement> children = new ArrayList<ParseElement>();

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public String getParseMethod() {
        return parseMethod;
    }

    public void setParseMethod(String parseMethod) {
        this.parseMethod = parseMethod;
    }

    public String getBuildMethod() {
        return buildMethod;
    }

    public void setBuildMethod(String buildMethod) {
        this.buildMethod = buildMethod;
    }

    public List<ParseElement> getChildren() {
        return children;
    }

    public void setChildren(List<ParseElement> children) {
        this.children = children;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLllvar() {
        return lllvar;
    }

    public void setLllvar(String lllvar) {
        this.lllvar = lllvar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLen() {
        return len;
    }

    public void setLen(String len) {
        this.len = len;
    }

    public ParseElement clone() {
        ParseElement clone = new ParseElement();
        clone.setCode(code);
        clone.setComments(comments);
        clone.setId(id);
        clone.setLen(len);
        clone.setLllvar(lllvar);
        clone.setTag(tag);
        clone.setValue(value);
        clone.setBuildMethod(buildMethod);
        clone.setParseMethod(parseMethod);
        clone.setChildren(children);
        clone.setTargetClass(targetClass);
        clone.setParseMethodParam(parseMethodParam);
        clone.setBuildMethodParam(buildMethodParam);
        return clone;
    }

    @Override
    public String toString() {
        return "ParseElement [id=" + id + ", tag=" + tag + ", len=" + len
                + ", value=" + value + ", lllvar=" + lllvar + ", code=" + code
                + ", comments=" + comments + ", targetClass=" + targetClass
                + ", parseMethod=" + parseMethod + ", buildMethod="
                + buildMethod + ", parseMethodParam=" + parseMethodParam
                + ", buildMethodParam=" + buildMethodParam + ", children="
                + children + "]";
    }

}
