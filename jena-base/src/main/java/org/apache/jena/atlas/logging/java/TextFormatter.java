/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.atlas.logging.java;

import java.text.MessageFormat ;
import java.util.Date ;
import java.util.logging.Formatter ;
import java.util.logging.LogManager ;
import java.util.logging.LogRecord ;

/** A pattern-driven log formatter.
 * inspired by Log4j's PatternLayout
 * Set a different output pattern with {@code .format}.
 * <p>
 * The default format is {@code "%5$tT %3$-5s %2$-20s :: %6$s\n"}.
 * <ul>
 * <li>"%5$tT.%5$tL %3$-5s %2$-20s :: %6$s\n" for milliseconds.
 * <li>"%tF %5$tT.%5$tL %3$-5s %2$-20s :: %6$s\n" for date
 * </ul>
 * <p>
 * The log message formatting call is:
 * <pre>
 *     String.format(format, 
 *                   loggerName,                        // 1
 *                   loggerNameShort,                   // 2
 *                   record.getLevel(),                 // 3
 *                   Thread.currentThread().getName(),  // 4
 *                   new Date(record.getMillis()),      // 5
 *                   formatted$) ;                      // 6
 * </pre>
 * where {@code formatted$} is the {@link LogRecord} message string after parameters have been processed. 
 */
public class TextFormatter extends Formatter
{
    // %tT (%5$tT) is %5$tH:%5$tM:%5$tS
    // %tF is 2008-11-22 "%tY-%tm-%td"
    private String format = "%5$tT %3$-5s %2$-20s :: %6$s\n" ;
    
    public TextFormatter() {
        LogManager manager = LogManager.getLogManager() ;
        String cname = getClass().getName();
        
        String fmt = manager.getProperty(cname+".format") ;
        if ( fmt != null ) {
            if ( ! fmt.endsWith("\n") )
                fmt = fmt + "\n" ;
            format = fmt ;
        }
    }
    
    @Override
    public String format(LogRecord record) {
        String loggerName = record.getLoggerName();
        if(loggerName == null) {
            loggerName = "root";
        }
        
        int i = loggerName.lastIndexOf('.') ; 
        String loggerNameShort = loggerName.substring(i+1) ;
            
        String formatted$ = record.getMessage() ;
        if ( record.getParameters() != null )
            formatted$ = MessageFormat.format(formatted$, record.getParameters()) ;
        
        return String.format(format, 
                             loggerName,                        // 1
                             loggerNameShort,                   // 2
                             record.getLevel(),                 // 3
                             Thread.currentThread().getName(),  // 4
                             new Date(record.getMillis()),      // 5
                             formatted$) ;                      // 6
    }
}
