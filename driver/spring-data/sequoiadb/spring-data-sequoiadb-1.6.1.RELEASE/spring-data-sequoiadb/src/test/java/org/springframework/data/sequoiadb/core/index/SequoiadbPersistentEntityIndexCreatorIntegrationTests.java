/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.sequoiadb.core.index;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.sequoiadb.core.SequoiadbOperations;
import org.springframework.data.sequoiadb.test.util.SequoiadbVersionRule;
import org.springframework.data.util.Version;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration tests for {@link SequoiadbPersistentEntityIndexCreator}.
 * 


 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SequoiadbPersistentEntityIndexCreatorIntegrationTests {

	public static @ClassRule
	SequoiadbVersionRule version = SequoiadbVersionRule.atLeast(new Version(2, 6));

	@Autowired @Qualifier("sequoiadb1")
    SequoiadbOperations templateOne;

	@Autowired @Qualifier("sequoiadb2")
    SequoiadbOperations templateTwo;

	@After
	public void cleanUp() {
		templateOne.dropCollection(SampleEntity.class);
		templateTwo.dropCollection(SampleEntity.class);
	}

	@Test
	public void createsIndexForConfiguredMappingContextOnly() {

		List<IndexInfo> indexInfo = templateOne.indexOps(SampleEntity.class).getIndexInfo();
		assertThat(indexInfo, hasSize(greaterThan(0)));
		assertThat(indexInfo, Matchers.<IndexInfo> hasItem(hasProperty("name", is("prop"))));

		indexInfo = templateTwo.indexOps("sampleEntity").getIndexInfo();
		assertThat(indexInfo, hasSize(0));
	}
}