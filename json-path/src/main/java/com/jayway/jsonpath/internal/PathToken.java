/*
 * Copyright 2011 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jayway.jsonpath.internal;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.InvalidModelException;
import com.jayway.jsonpath.internal.filter.FilterFactory;
import com.jayway.jsonpath.internal.filter.PathTokenFilter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Kalle Stenflo
 */
public class PathToken {
    
    private static final Pattern ARRAY_INDEX_PATTERN = Pattern.compile("\\[(\\d+)]");

    private final String fragment;

    private final int tokenIndex;

    private final boolean endToken;

    public PathToken(String fragment, int tokenIndex, boolean isEndToken) {
        this.fragment = fragment;
        this.tokenIndex = tokenIndex;
        this.endToken = isEndToken;
    }

    public PathTokenFilter getFilter(){
        return FilterFactory.createFilter(this);
    }

    public Object filter(Object model, Object root, Configuration configuration){
        return FilterFactory.createFilter(this).filter(model, root, configuration);
    }

    public Object apply(Object model, Object root, Configuration configuration){
        return FilterFactory.createFilter(this).getRef(model, root, configuration);
    }

    public String getFragment() {
        return fragment;
    }

    public boolean isRootToken(){
        return this.tokenIndex == 0;
    }

    public boolean isEndToken(){
        return this.endToken;
    }

    public boolean isArrayIndexToken(){
        return ARRAY_INDEX_PATTERN.matcher(fragment).matches();   
    }
    
    public int getArrayIndex(){
        Matcher matcher = ARRAY_INDEX_PATTERN.matcher(fragment);
        if(matcher.find()){
            return Integer.parseInt(matcher.group(1));
        }
        else throw new InvalidModelException("Could not get array index from fragment " + fragment);
    }
}
