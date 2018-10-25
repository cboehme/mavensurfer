/*
 * Copyright 2018 Christoph Böhme
 *
 * Licensed under the Apache License, Version 2.0 the "License";
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.b3e.mavensurfer.browser.db;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import net.b3e.mavensurfer.browser.configuration.ConfigData;

@ApplicationScoped
public class ConfigDataRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void store(ConfigData configData) {

        requireNonNull(configData);

        entityManager.merge(ConfigDataBean.createFrom(configData));
    }

    @Transactional
    public Optional<ConfigData> load(String name) {

        requireNonNull(name);

        return findConfigDataBeanWith(name)
                .map(ConfigDataBean::toConfigData);
    }

    private Optional<ConfigDataBean> findConfigDataBeanWith(String name) {

        return Optional.ofNullable(entityManager.find(ConfigDataBean.class, name));
    }

    @Transactional
    public boolean delete(String name) {

        requireNonNull(name);

        final ConfigDataBean beanRef;
        try {
            beanRef = entityManager.getReference(ConfigDataBean.class, name);
            entityManager.remove(beanRef);
        } catch (EntityNotFoundException e) {
            return false;
        }
        return true;
    }

}
