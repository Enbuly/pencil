package com.rain.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayParamFlowItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * sentinel限流
 *
 * @author zhangzhenyan
 * 2020-06-18
 **/
@Configuration
public class GatewayConfig {

    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    public GatewayConfig(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                         ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
        // Register the block exception handler for Spring Cloud Gateway.
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }

    @Bean
    @Order(-1)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    @PostConstruct
    public void doInit() {
        initCustomizedApis();
        initGatewayRules();
    }

    //ApiDefinition：用户自定义的 API 定义分组，可以看做是一些 URL 匹配的组合。
    //比如我们可以定义一个 API 叫 my_api，请求 path 模式为 /foo/** 和 /baz/**
    //的都归到 my_api 这个 API 分组下面。限流的时候可以针对这个自定义的 API 分组维度进行限流。
    private void initCustomizedApis() {
        Set<ApiDefinition> definitions = new HashSet<>();
        ApiDefinition api1 = new ApiDefinition("some_customized_api")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    add(new ApiPathPredicateItem().setPattern("/apple/**")
                            .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                }});
        ApiDefinition api2 = new ApiDefinition("another_customized_api")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    add(new ApiPathPredicateItem().setPattern("/**")
                            .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                }});
        definitions.add(api1);
        definitions.add(api2);
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }

    private void initGatewayRules() {
        //GatewayFlowRule：网关限流规则，针对 API Gateway 的场景定制的限流规则，
        //可以针对不同 route 或自定义的 API 分组进行限流，支持针对请求中的
        //参数、Header、来源 IP 等进行定制化的限流。
        Set<GatewayFlowRule> rules = new HashSet<>();

        //普通流控规则
        rules.add(new GatewayFlowRule()
                //resource：资源名称，可以是网关中的 route 名称或者用户自定义的 API 分组名称。
                .setResource("service-apple")

                //count：限流阈值
                .setCount(30)

                //intervalSec：统计时间窗口，单位是秒，默认是 1 秒。
                .setIntervalSec(1)

                //burst：应对突发请求时额外允许的请求数目。
                .setBurst(2)
        );

        //普通流控规则
        rules.add(new GatewayFlowRule()
                .setResource("service-banana")
                .setCount(30)
                .setIntervalSec(1)
                .setBurst(2)
        );

        //ip流控规则
        rules.add(new GatewayFlowRule()
                .setResource("service-apple")
                .setCount(6)
                .setIntervalSec(2)
                .setBurst(2)
                //paramItem：参数限流配置。若不提供，则代表不针对参数进行限流，
                //该网关规则将会被转换成普通流控规则；否则会转换成热点规则。

                //parseStrategy：从请求中提取参数的策略，目前支持提取来源
                //IP(PARAM_PARSE_STRATEGY_CLIENT_IP)
                //Host(PARAM_PARSE_STRATEGY_HOST)
                //任意 Header(PARAM_PARSE_STRATEGY_HEADER)
                //和任意 URL 参数（PARAM_PARSE_STRATEGY_URL_PARAM）四种模式。

                //fieldName：若提取策略选择 Header 模式或 URL 参数模式，
                //则需要指定对应的 header 名称或 URL 参数名称。

                //pattern：参数值的匹配模式，只有匹配该模式的请求属性值会纳入统计和流控；
                //若为空则统计该请求属性的所有值。（1.6.2 版本开始支持）

                //matchStrategy：参数值的匹配策略，目前支持精确匹配（PARAM_MATCH_STRATEGY_EXACT）、
                //子串匹配（PARAM_MATCH_STRATEGY_CONTAINS）和正则匹配（PARAM_MATCH_STRATEGY_REGEX）。
                .setParamItem(new GatewayParamFlowItem()
                        .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)
                )
        );

        //host
        rules.add(new GatewayFlowRule()
                .setResource("service-apple")
                .setCount(2)
                .setIntervalSec(1)
                //controlBehavior：流量整形的控制效果，
                //同限流规则的 controlBehavior 字段，
                //目前支持快速失败和匀速排队两种模式，默认是快速失败。
                .setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER)
                //maxQueueingTimeoutMs：匀速排队模式下的最长排队时间，单位是毫秒，仅在匀速排队模式下生效。
                .setMaxQueueingTimeoutMs(600)
                .setParamItem(new GatewayParamFlowItem()
                        .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_HOST)
                )
        );

        //url
        rules.add(new GatewayFlowRule()
                .setResource("service-banana")
                .setCount(6)
                .setIntervalSec(1)
                .setBurst(2)
                //.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER)
                //.setMaxQueueingTimeoutMs(600)
                .setParamItem(new GatewayParamFlowItem()
                        .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_URL_PARAM)
                        .setFieldName("name")
                        .setMatchStrategy(SentinelGatewayConstants.PARAM_MATCH_STRATEGY_CONTAINS)
                )
        );

        //自定义的API分组
        rules.add(new GatewayFlowRule()
                .setResource("some_customized_api")
                //resourceMode：规则是针对 API Gateway 的 route（RESOURCE_MODE_ROUTE_ID）
                //还是用户在 Sentinel 中定义的 API 分组（RESOURCE_MODE_CUSTOM_API_NAME），默认是 route。
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)
                .setCount(5)
                .setIntervalSec(1)
        );
        GatewayRuleManager.loadRules(rules);
    }
}
