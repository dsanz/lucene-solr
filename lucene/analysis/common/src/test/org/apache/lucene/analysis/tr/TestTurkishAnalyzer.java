package org.apache.lucene.analysis.tr;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

public class TestTurkishAnalyzer extends BaseTokenStreamTestCase {
  /** This test fails with NPE when the 
   * stopwords file is missing in classpath */
  public void testResourcesAvailable() {
    new TurkishAnalyzer();
  }
  
  /** test stopwords and stemming */
  public void testBasics() throws IOException {
    Analyzer a = new TurkishAnalyzer();
    // stemming
    checkOneTerm(a, "ağacı", "ağaç");
    checkOneTerm(a, "ağaç", "ağaç");
    // stopword
    assertAnalyzesTo(a, "dolayı", new String[] {});
    // apostrophes
    checkOneTerm(a, "Kıbrıs'ta", "kıbrıs");
    assertAnalyzesTo(a, "Van Gölü'ne", new String[]{"van", "göl"});
  }
  
  /** test use of exclusion set */
  public void testExclude() throws IOException {
    CharArraySet exclusionSet = new CharArraySet(asSet("ağacı"), false);
    Analyzer a = new TurkishAnalyzer(TurkishAnalyzer.getDefaultStopSet(), exclusionSet);
    checkOneTerm(a, "ağacı", "ağacı");
    checkOneTerm(a, "ağaç", "ağaç");
  }
  
  /** blast some random strings through the analyzer */
  public void testRandomStrings() throws Exception {
    checkRandomData(random(), new TurkishAnalyzer(), 1000*RANDOM_MULTIPLIER);
  }

  public void testBackcompat40() throws IOException {
    TurkishAnalyzer a = new TurkishAnalyzer();
    a.setVersion(Version.LUCENE_4_6_1);
    // this is just a test to see the correct unicode version is being used, not actually testing hebrew
    assertAnalyzesTo(a, "א\"א", new String[] {"א", "א"});
  }
}
