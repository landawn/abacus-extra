/*
 * Copyright (C) 2015 HaiYang Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.landawn.abacus.remote;

import java.util.Map;

import com.landawn.abacus.annotation.Internal;
import com.landawn.abacus.annotation.Type;
import com.landawn.abacus.remote.RemoteTask.RunMode;
import com.landawn.abacus.util.N;

// TODO: Auto-generated Javadoc
/**
 *
 * @author Haiyang Li
 * @since 0.8
 */
@Internal
@Deprecated
public class RemoteExecutionRequest {

    private String requestHost;

    // TODO
    private String requestId;

    // TODO
    private RunMode runMode;

    private long requestTime;

    // TODO: quartz scheduler?    
    private String schedule;

    private String className;

    private Object parameter;

    private Map<String, byte[]> classBytesMap;

    //    public static void main(String[] args) {
    //        CodeGenerator.printClassMethod(RemoteExecutionRequest.class, true, true, true, null);
    //    }

    /**
     * Gets the request host.
     *
     * @return
     */
    public String getRequestHost() {
        return requestHost;
    }

    /**
     * Sets the request host.
     *
     * @param requestHost
     * @return
     */
    public RemoteExecutionRequest setRequestHost(String requestHost) {
        this.requestHost = requestHost;

        return this;
    }

    /**
     * Gets the request id.
     *
     * @return
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Sets the request id.
     *
     * @param requestId
     * @return
     */
    public RemoteExecutionRequest setRequestId(String requestId) {
        this.requestId = requestId;

        return this;
    }

    /**
     * Gets the run mode.
     *
     * @return
     */
    public com.landawn.abacus.remote.RemoteTask.RunMode getRunMode() {
        return runMode;
    }

    /**
     * Sets the run mode.
     *
     * @param runMode
     * @return
     */
    public RemoteExecutionRequest setRunMode(com.landawn.abacus.remote.RemoteTask.RunMode runMode) {
        this.runMode = runMode;

        return this;
    }

    /**
     * Gets the request time.
     *
     * @return
     */
    public long getRequestTime() {
        return requestTime;
    }

    /**
     * Sets the request time.
     *
     * @param requestTime
     * @return
     */
    public RemoteExecutionRequest setRequestTime(long requestTime) {
        this.requestTime = requestTime;

        return this;
    }

    /**
     * Gets the schedule.
     *
     * @return
     */
    public String getSchedule() {
        return schedule;
    }

    /**
     * Sets the schedule.
     *
     * @param schedule
     * @return
     */
    public RemoteExecutionRequest setSchedule(String schedule) {
        this.schedule = schedule;

        return this;
    }

    /**
     * Gets the class name.
     *
     * @return
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sets the class name.
     *
     * @param className
     * @return
     */
    public RemoteExecutionRequest setClassName(String className) {
        this.className = className;

        return this;
    }

    /**
     * Gets the parameter.
     *
     * @return
     */
    public Object getParameter() {
        return parameter;
    }

    /**
     * Sets the parameter.
     *
     * @param parameter
     * @return
     */
    public RemoteExecutionRequest setParameter(Object parameter) {
        this.parameter = parameter;

        return this;
    }

    /**
     * Gets the class bytes map.
     *
     * @return
     */
    @Type("Map<String, byte[]>")
    public Map<String, byte[]> getClassBytesMap() {
        return classBytesMap;
    }

    /**
     * Sets the class bytes map.
     *
     * @param classBytesMap
     * @return
     */
    public RemoteExecutionRequest setClassBytesMap(Map<String, byte[]> classBytesMap) {
        this.classBytesMap = classBytesMap;

        return this;
    }

    public RemoteExecutionRequest copy() {
        final RemoteExecutionRequest copy = new RemoteExecutionRequest();

        copy.requestHost = this.requestHost;
        copy.requestId = this.requestId;
        copy.runMode = this.runMode;
        copy.requestTime = this.requestTime;
        copy.schedule = this.schedule;
        copy.className = this.className;
        copy.parameter = this.parameter;
        copy.classBytesMap = this.classBytesMap;

        return copy;
    }

    @Override
    public int hashCode() {
        int h = 17;
        h = 31 * h + N.hashCode(requestHost);
        h = 31 * h + N.hashCode(requestId);
        h = 31 * h + N.hashCode(runMode);
        h = 31 * h + N.hashCode(requestTime);
        h = 31 * h + N.hashCode(schedule);
        h = 31 * h + N.hashCode(className);
        h = 31 * h + N.hashCode(parameter);
        h = 31 * h + N.hashCode(classBytesMap);

        return h;
    }

    /**
     *
     * @param obj
     * @return true, if successful
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof RemoteExecutionRequest) {
            final RemoteExecutionRequest other = (RemoteExecutionRequest) obj;

            return N.equals(requestHost, other.requestHost) && N.equals(requestId, other.requestId) && N.equals(runMode, other.runMode)
                    && N.equals(requestTime, other.requestTime) && N.equals(schedule, other.schedule) && N.equals(className, other.className)
                    && N.equals(parameter, other.parameter) && N.equals(classBytesMap, other.classBytesMap);
        }

        return false;
    }

    @Override
    public String toString() {
        return "{requestHost=" + N.toString(requestHost) + ", requestId=" + N.toString(requestId) + ", runMode=" + N.toString(runMode) + ", requestTime="
                + N.toString(requestTime) + ", schedule=" + N.toString(schedule) + ", className=" + N.toString(className) + ", parameter="
                + N.toString(parameter) + ", classBytesMap=" + N.toString(classBytesMap) + "}";
    }

}
