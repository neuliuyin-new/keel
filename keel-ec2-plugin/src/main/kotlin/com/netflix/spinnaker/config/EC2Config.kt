/*
 * Copyright 2018 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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

package com.netflix.spinnaker.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.spinnaker.keel.api.ec2.Cluster
import com.netflix.spinnaker.keel.api.ec2.SecurityGroup
import com.netflix.spinnaker.keel.clouddriver.CloudDriverCache
import com.netflix.spinnaker.keel.clouddriver.CloudDriverService
import com.netflix.spinnaker.keel.ec2.resource.ClusterHandler
import com.netflix.spinnaker.keel.ec2.resource.SecurityGroupHandler
import com.netflix.spinnaker.keel.orca.OrcaService
import com.netflix.spinnaker.keel.plugin.ResourceValidator
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
@ConditionalOnProperty("keel.plugins.ec2.enabled")
class EC2Config {

  @Bean
  fun clusterHandler(
    cloudDriverService: CloudDriverService,
    cloudDriverCache: CloudDriverCache,
    orcaService: OrcaService,
    clock: Clock,
    objectMapper: ObjectMapper,
    validators: List<ResourceValidator<*>>
  ): ClusterHandler =
    ClusterHandler(
      cloudDriverService,
      cloudDriverCache,
      orcaService,
      clock,
      objectMapper,
      validators.filter { it.handles(Cluster::class) }.map { it as ResourceValidator<Cluster> }
    )

  @Bean
  fun securityGroupHandler(
    cloudDriverService: CloudDriverService,
    cloudDriverCache: CloudDriverCache,
    orcaService: OrcaService,
    objectMapper: ObjectMapper,
    validators: List<ResourceValidator<*>>
  ): SecurityGroupHandler =
    SecurityGroupHandler(
      cloudDriverService,
      cloudDriverCache,
      orcaService,
      objectMapper,
      validators.filter { it.handles(SecurityGroup::class) }.map { it as ResourceValidator<SecurityGroup> }
    )
}
