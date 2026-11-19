export default {
  nav: {
    home: 'Home',
    search: 'Search',
    about: 'About Us'
  },
  hero: {
    title: 'Open Science Resource Discovery Platform',
    subtitle: 'Aggregating global academic literature, data, software and facilities',
    tagline: 'Open Science Starts Here',
    searchPlaceholder: 'Search articles, data, software or facilities...'
  },
  stats: {
    articleTotal: 'Articles Total',
    scienceDataTotal: 'Science Data Total',
    softwareTotal: 'Software Total',
    facilityTotal: 'Facility Total'
  },
  resources: {
    title: 'Open Science Resource Framework',
    subtitle: 'Explore resources across multiple scientific domains.'
  },
  headlines: {
    prefix: 'Resource',
    prefixHighlight: 'Highlights',
    title: 'Literature',
    contact: 'Custom headline service contact: ',
    zongjie: 'Summary',
    more: 'MORE'
  },
  headlines2: {
    prefix: 'Tech',
    prefixHighlight: 'Frontier',
    title: 'Science Data',
    contact: 'Custom service contact: ',
    zongjie: 'Summary',
    more: 'MORE'
  },
  headlines3: {
    prefix: 'Scientific',
    prefixHighlight: 'Software',
    title: 'Science Software',
    contact: 'Custom service contact: ',
    zongjie: 'Summary',
    more: 'MORE'
  },
  headlines4: {
    prefix: 'Large',
    prefixHighlight: 'Facility',
    title: 'Large Scientific Facility',
    contact: 'Custom service contact: ',
    zongjie: 'Summary',
    more: 'MORE'
  },
  search: {
    tab: {
      article: 'Article',
      sciencedata: 'Science Data',
      software: 'Software',
      facility: 'Facility'
    },
    filterTitle: 'Filters',
    searchBtn: 'Search',
    filterConditions: 'Filter Conditions',
    clearAll: 'Clear All',
    totalResults: 'Found {n} results',
    placeholder: {
      article: 'Search Articles',
      sciencedata: 'Search Science Data',
      software: 'Search Software',
      facility: 'Search Facility'
    },
    agg: {
      classificationFirst: 'Category L1',
      classificationSecond: 'Category L2',
      year: 'Year',
      subject: 'Research Area',
      journal: 'Journal',
      datasetType: 'Dataset Type',
      type: 'Type',
      resourceType: 'Resource Type',
      category: 'Category',
      language: 'Language',
      license: 'License',
      country: 'Country',
      institution: 'Hosting Institution'
    }
  },
  contactEmail: 'contact@open-explore.org',
  homepage: {
    bannerSlides: [
      {
        title: 'Academic Literature',
        description: 'Aggregating global open-access academic resources across multiple disciplines',
        image: 'https://img0.baidu.com/it/u=4279917707,1303528668&fm=253&app=138&f=JPEG?w=500&h=500',
        url: '#'
      },
      {
        title: 'Science Data',
        description: 'Curating open scientific datasets across disciplines for reproducible research',
        image: 'https://img0.baidu.com/it/u=4279917707,1303528668&fm=253&app=138&f=JPEG?w=500&h=500',
        url: '#'
      },
      {
        title: 'Research Infrastructure',
        description: 'Connecting large-scale facilities and open science software resources',
        image: 'https://img0.baidu.com/it/u=4279917707,1303528668&fm=253&app=138&f=JPEG?w=500&h=500',
        url: '#'
      }
    ],
    resourceTypes: [
      { biaoti: 'Open Access Literature', xiangqing: 'Global open-access journals, conference papers and preprints' },
      { biaoti: 'Scientific Data Repositories', xiangqing: 'Structured and unstructured open scientific datasets across fields' },
      { biaoti: 'Scientific Software Tools', xiangqing: 'Open-source scientific computing, analysis and visualization software' },
      { biaoti: 'Large Research Facilities', xiangqing: 'Major scientific infrastructure and open sharing platforms' },
      { biaoti: 'Open Educational Resources', xiangqing: 'Open courses, textbooks and research training materials' },
      { biaoti: 'Academic Network', xiangqing: 'Connecting global open science communities and platforms' },
      { biaoti: 'Policy & Standards', xiangqing: 'Tracking open science policy developments and data standards' }
    ],
    footer: {
      brandName: 'Open Explore',
      institutionName: 'Open Science Resource Discovery Platform',
      address: 'Zhongguancun, Haidian District, Beijing',
      email: 'contact@open-explore.org',
      copyright: '© 2026 Open Explore'
    }
  },
  footer: {
    contactUs: 'Contact Us',
    copyright: '© 2026 OpenScience¹⁰. All rights reserved.'
  },
  about: {
    eyebrow: 'OPEN SCIENCE · RESOURCE DISCOVERY',
    title: 'About Open Explore',
    subtitle: 'A resource discovery platform for the open science ecosystem, bringing literature, scientific data, software and large research facilities into one search.',
    ctaSearch: 'Start searching',
    ctaContact: 'Contact us',
    introEyebrow: 'ABOUT THE PLATFORM',
    overview: {
      title: 'At a glance',
      items: [
        { label: 'Resource types', value: '4 categories' },
        { label: 'Interface languages', value: '5 languages' },
        { label: 'Search engine', value: 'Elasticsearch' },
        { label: 'Service API', value: 'RESTful API' }
      ]
    },
    introduction: [
      'Open Explore is a resource discovery platform for the open science ecosystem. It aggregates four core resource types — academic literature, scientific data, scientific software and large research facilities — and provides a unified search experience on top of a full-text search engine.',
      'The platform is built on Elasticsearch, with a Vue.js frontend and a Spring Boot backend. The interface is available in Chinese, English, Spanish, French and Russian, and supports faceted filtering, aggregation statistics and record-level detail browsing.',
      'Our goal is to lower the barrier to research resources and to speed up the global adoption of open science. Researchers, data stewards and anyone curious about open science can use Open Explore to find and access the resources they need.'
    ],
    capabilities: {
      title: 'Four resource types, one search',
      subtitle: 'Metadata from literature databases, data repositories, code hosting platforms and facility catalogues is normalized into a single index.',
      countLabel: 'records',
      items: [
        { key: 'article', name: 'Academic literature', desc: 'Bibliographic records, abstracts and open access links for journal articles, conference papers and preprints.' },
        { key: 'sciencedata', name: 'Scientific data', desc: 'Dataset metadata organized by discipline, including dataset type, time range, spatial coverage and licence.' },
        { key: 'software', name: 'Scientific software', desc: 'Version, programming language, licence and repository URL for research computing, analysis and visualization tools.' },
        { key: 'facility', name: 'Large facilities', desc: 'Hosting institutions, contact details and application guides for major research infrastructures and shared platforms.' }
      ]
    },
    platform: {
      title: 'Platform capabilities',
      subtitle: 'From metadata ingestion to the search experience, the whole chain is built on open source components.',
      items: [
        { title: 'Unified search', desc: 'Multi-field full-text search on Elasticsearch, combining title, author, subject and source fields in a single query.', tags: ['Elasticsearch', 'Multi-field'] },
        { title: 'Faceted filtering', desc: 'Aggregations by year, subject classification, dataset type, licence and country, with live counts for every option.', tags: ['Aggregations', 'Facet'] },
        { title: 'Multilingual interface', desc: 'Chinese, English, Spanish, French and Russian interfaces driven by the same data source.', tags: ['Vue i18n', 'zh/en/es/fr/ru'] },
        { title: 'Open API', desc: 'Search, aggregation and detail endpoints served as REST + JSON for integration and reuse.', tags: ['REST', 'JSON'] }
      ]
    },
    team: {
      title: 'Team and responsibilities',
      subtitle: 'An interdisciplinary team covering data governance, platform engineering and product design, working module by module.',
      items: [
        { role: 'Platform architecture', desc: 'Technology choices, service boundaries and deployment, keeping search stable and scalable.' },
        { role: 'Search engine', desc: 'Index design, field mapping, and query and aggregation tuning.' },
        { role: 'Data governance', desc: 'Metadata specifications, field normalization and source quality checks.' },
        { role: 'Front-end experience', desc: 'Interaction and visual implementation of the search page, detail pages and multilingual UI.' },
        { role: 'Open API', desc: 'Public API design, documentation and integration support.' },
        { role: 'Quality & operations', desc: 'Monitoring, alerting, load testing and scheduled data refresh.' }
      ]
    },
    contact: {
      title: 'Build it with us',
      desc: 'Universities, research institutes, data repositories and open source communities are welcome to contribute metadata sources or report issues. Resource coverage and search quality keep improving with your feedback.',
      action: 'Send an email'
    },
    footer: '© 2026 Open Explore. Open Science, Knowledge Sharing.'
  }
}
