export default {
  nav: {
    home: '首页',
    search: '文献搜索',
    about: '关于我们'
  },
  hero: {
    title: '开放科学资源发现平台',
    subtitle: '汇聚全球学术文献、科学数据、软件与大型科研装置',
    tagline: '让开放科学触手可及',
    searchPlaceholder: '搜索学术文献、科学数据、科学软件或大型装置...'
  },
  stats: {
    articleTotal: '文献总量',
    scienceDataTotal: '科学数据总量',
    softwareTotal: '科学软件总量',
    facilityTotal: '科学装置总量'
  },
  resources: {
    title: '开放科学能力框架',
    subtitle: '聚合全球开放科学知识、数据与基础设施'
  },
  headlines: {
    prefix: '资源',
    prefixHighlight: '亮点',
    title: '文献',
    contact: '头条定制服务请联系：',
    zongjie: '总结',
    more: 'MORE'
  },
  headlines2: {
    prefix: '技术',
    prefixHighlight: '前沿',
    title: '科学数据',
    contact: '定制服务请联系：',
    zongjie: '总结',
    more: 'MORE'
  },
  headlines3: {
    prefix: '科学',
    prefixHighlight: '软件',
    title: '科学软件',
    contact: '定制服务请联系：',
    zongjie: '总结',
    more: 'MORE'
  },
  headlines4: {
    prefix: '大型',
    prefixHighlight: '装置',
    title: '大型科学装置',
    contact: '定制服务请联系：',
    zongjie: '总结',
    more: 'MORE'
  },
  search: {
    tab: {
      article: '文献',
      sciencedata: '科学数据',
      software: '科学软件',
      facility: '大型装置'
    },
    filterTitle: '筛选条件',
    searchBtn: '搜索',
    filterConditions: '已选条件',
    clearAll: '清除全部',
    totalResults: '共找到 {n} 条结果',
    placeholder: {
      article: '搜索文献',
      sciencedata: '搜索科学数据',
      software: '搜索科学软件',
      facility: '搜索大型装置'
    },
    agg: {
      classificationFirst: '分类 L1',
      classificationSecond: '分类 L2',
      year: '年份',
      subject: '研究领域',
      journal: '期刊',
      datasetType: '数据类型',
      type: '类型',
      resourceType: '资源类型',
      category: '类别',
      language: '编程语言',
      license: '许可证',
      country: '国家',
      institution: '托管机构'
    }
  },
  contactEmail: 'contact@open-explore.org',
  homepage: {
    bannerSlides: [
      {
        title: '学术文献',
        description: '聚合全球开放获取学术资源，涵盖多学科领域前沿研究',
        image: 'https://img0.baidu.com/it/u=4279917707,1303528668&fm=253&app=138&f=JPEG?w=500&h=500',
        url: '#'
      },
      {
        title: '科学数据',
        description: '汇集各学科开放科学数据集，支撑可重复性研究',
        image: 'https://img0.baidu.com/it/u=4279917707,1303528668&fm=253&app=138&f=JPEG?w=500&h=500',
        url: '#'
      },
      {
        title: '科研基础设施',
        description: '对接国内外大型科学装置与开放科学软件资源',
        image: 'https://img0.baidu.com/it/u=4279917707,1303528668&fm=253&app=138&f=JPEG?w=500&h=500',
        url: '#'
      }
    ],
    resourceTypes: [
      { biaoti: '开放获取文献', xiangqing: '聚合全球开放获取学术期刊、会议论文与预印本资源' },
      { biaoti: '科学数据仓储', xiangqing: '汇集各学科领域的结构化与非结构化开放科学数据集' },
      { biaoti: '科学软件工具', xiangqing: '收录开源科学计算、数据分析与可视化软件' },
      { biaoti: '大型科研装置', xiangqing: '汇聚国内外重大科技基础设施与开放共享平台' },
      { biaoti: '开放教育资源', xiangqing: '整合开放课程、教材与科研培训材料' },
      { biaoti: '学术交流网络', xiangqing: '链接全球开放科学社区与学术社交平台' },
      { biaoti: '政策与标准', xiangqing: '追踪开放科学政策动态与数据标准规范' }
    ],
    footer: {
      brandName: 'Open Explore',
      institutionName: '开放科学资源发现平台',
      address: '北京市海淀区中关村',
      email: 'contact@open-explore.org',
      copyright: '© 2026 Open Explore'
    }
  },
  footer: {
    contactUs: '联系方式',
    copyright: '© 2026 OpenScience¹⁰ 版权所有'
  },
  about: {
    eyebrow: 'OPEN SCIENCE · 开放科学资源发现',
    title: '关于 Open Explore',
    subtitle: '面向开放科学生态的资源发现平台，统一检索学术文献、科学数据、科学软件与大型科研装置。',
    ctaSearch: '开始检索',
    ctaContact: '联系我们',
    introEyebrow: 'ABOUT THE PLATFORM',
    overview: {
      title: '平台概览',
      items: [
        { label: '资源类型', value: '4 类' },
        { label: '界面语言', value: '5 种' },
        { label: '检索引擎', value: 'Elasticsearch' },
        { label: '服务接口', value: 'RESTful API' }
      ]
    },
    introduction: [
      'Open Explore 是一个面向开放科学（Open Science）生态的资源发现平台，聚合四种核心资源：学术文献、科学数据、科学软件与大型科研装置，并通过全文检索引擎提供统一的检索与发现体验。',
      '平台基于 Elasticsearch 构建，前端使用 Vue.js，后端基于 Spring Boot；界面支持中文、英文、西班牙语、法语与俄语，提供分面筛选、聚合统计与资源详情浏览。',
      '我们的目标是降低科研资源的获取门槛，加快开放科学的全球化进程。科研人员、数据管理者以及关注开放科学的公众，都可以通过 Open Explore 高效地发现并访问所需资源。'
    ],
    capabilities: {
      title: '四类资源，一次检索',
      subtitle: '来自文献库、数据仓储、代码托管平台与科研装置目录的元数据，经统一清洗与字段规范化后进入同一索引。',
      countLabel: '条记录',
      items: [
        { key: 'article', name: '学术文献', desc: '期刊论文、会议论文与预印本的题录、摘要与开放获取链接。' },
        { key: 'sciencedata', name: '科学数据', desc: '按学科整理的数据集元数据，涵盖数据类型、时间范围、空间范围与许可协议。' },
        { key: 'software', name: '科学软件', desc: '科研计算、数据分析与可视化工具的版本、开发语言、许可证与代码仓库地址。' },
        { key: 'facility', name: '大型科研装置', desc: '重大科技基础设施与开放共享平台的托管机构、联系方式与申请指南。' }
      ]
    },
    platform: {
      title: '平台能力',
      subtitle: '从元数据接入到检索体验的完整链路，全部基于开源组件构建。',
      items: [
        { title: '统一检索', desc: '基于 Elasticsearch 的多字段全文检索，支持题名、作者、主题、来源等字段的组合查询。', tags: ['Elasticsearch', 'Multi-field'] },
        { title: '分面筛选', desc: '按年份、学科分类、数据类型、许可证、国家/地区聚合，实时统计各选项的命中数量。', tags: ['Aggregations', 'Facet'] },
        { title: '多语言界面', desc: '中文、English、Español、Français、Русский 五种界面语言，检索与展示同源同步。', tags: ['Vue i18n', 'zh/en/es/fr/ru'] },
        { title: '开放接口', desc: '检索、聚合与详情接口以 REST + JSON 形式提供，便于第三方系统集成与二次开发。', tags: ['REST', 'JSON'] }
      ]
    },
    team: {
      title: '团队与分工',
      subtitle: '由数据治理、平台研发与产品设计组成的跨学科团队，按模块协作推进。',
      items: [
        { role: '平台架构', desc: '技术选型、服务拆分与部署方案，保障检索服务的稳定性与可扩展性。' },
        { role: '检索引擎', desc: '索引结构设计、字段映射、查询与聚合策略调优。' },
        { role: '数据治理', desc: '元数据规范制定、字段映射清洗与来源质量校验。' },
        { role: '前端体验', desc: '检索页、详情页与多语言界面的交互与视觉实现。' },
        { role: '开放接口', desc: '对外 API 的设计、文档编写与接入支持。' },
        { role: '质量与运维', desc: '监控告警、性能压测与数据更新任务的日常维护。' }
      ]
    },
    contact: {
      title: '参与共建',
      desc: '欢迎高校、科研机构、数据仓储与开源社区提供元数据源或反馈使用问题，我们会持续扩充资源覆盖与检索能力。',
      action: '发送邮件'
    },
    footer: '© 2026 Open Explore. 开放科学，知识共享。'
  }
}
