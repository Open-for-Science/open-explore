export default {
  nav: {
    home: 'Главная',
    search: 'Поиск',
    about: 'О нас'
  },
  hero: {
    title: 'Платформа для поиска ресурсов открытой науки',
    subtitle: 'Объединение глобальной академической литературы, данных, программного обеспечения и установок',
    tagline: 'Открытая наука начинается здесь',
    searchPlaceholder: 'Поиск статей, данных, ПО или установок...'
  },
  stats: {
    articleTotal: 'Всего статей',
    scienceDataTotal: 'Всего научных данных',
    softwareTotal: 'Всего программ',
    facilityTotal: 'Всего установок'
  },
  resources: {
    title: 'Структура ресурсов открытой науки',
    subtitle: 'Исследуйте ресурсы в различных научных областях.'
  },
  headlines: {
    prefix: 'Ресурсы',
    prefixHighlight: 'Главное',
    title: 'Литература',
    contact: 'Служба индивидуальных заголовков: ',
    zongjie: 'Резюме',
    more: 'ЕЩЁ'
  },
  headlines2: {
    prefix: 'Технологии',
    prefixHighlight: 'Граница',
    title: 'Научные данные',
    contact: 'Индивидуальный сервис: ',
    zongjie: 'Резюме',
    more: 'ЕЩЁ'
  },
  headlines3: {
    prefix: 'Программное',
    prefixHighlight: 'обеспечение',
    title: 'Научное ПО',
    contact: 'Индивидуальный сервис: ',
    zongjie: 'Резюме',
    more: 'ЕЩЁ'
  },
  headlines4: {
    prefix: 'Крупная',
    prefixHighlight: 'установка',
    title: 'Крупная научная установка',
    contact: 'Индивидуальный сервис: ',
    zongjie: 'Резюме',
    more: 'ЕЩЁ'
  },
  search: {
    tab: {
      article: 'Статья',
      sciencedata: 'Научные Данные',
      software: 'ПО',
      facility: 'Установка'
    },
    filterTitle: 'Фильтры',
    searchBtn: 'Поиск',
    filterConditions: 'Условия Фильтра',
    clearAll: 'Очистить Всё',
    totalResults: 'Найдено {n} результатов',
    placeholder: {
      article: 'Поиск Статей',
      sciencedata: 'Поиск Данных',
      software: 'Поиск ПО',
      facility: 'Поиск Установки'
    },
    agg: {
      classificationFirst: 'Категория L1',
      classificationSecond: 'Категория L2',
      year: 'Год',
      datasetType: 'Тип Данных',
      type: 'Тип'
    }
  },
  contactEmail: 'contact@open-explore.org',
  homepage: {
    bannerSlides: [
      {
        title: 'Академическая литература',
        description: 'Объединение глобальных открытых академических ресурсов по множеству дисциплин',
        image: 'https://img0.baidu.com/it/u=4279917707,1303528668&fm=253&app=138&f=JPEG?w=500&h=500',
        url: '#'
      },
      {
        title: 'Научные данные',
        description: 'Курирование открытых научных наборов данных для воспроизводимых исследований',
        image: 'https://img0.baidu.com/it/u=4279917707,1303528668&fm=253&app=138&f=JPEG?w=500&h=500',
        url: '#'
      },
      {
        title: 'Исследовательская инфраструктура',
        description: 'Подключение крупных установок и ресурсов открытого программного обеспечения',
        image: 'https://img0.baidu.com/it/u=4279917707,1303528668&fm=253&app=138&f=JPEG?w=500&h=500',
        url: '#'
      }
    ],
    resourceTypes: [
      { biaoti: 'Открытая литература', xiangqing: 'Глобальные журналы, конференции и препринты открытого доступа' },
      { biaoti: 'Хранилища научных данных', xiangqing: 'Структурированные и неструктурированные открытые научные наборы данных' },
      { biaoti: 'Инструменты научного ПО', xiangqing: 'Открытое научное ПО для вычислений, анализа и визуализации' },
      { biaoti: 'Крупные научные установки', xiangqing: 'Крупные научные инфраструктуры и платформы открытого обмена' },
      { biaoti: 'Открытые образовательные ресурсы', xiangqing: 'Открытые курсы, учебники и учебные материалы' },
      { biaoti: 'Академическая сеть', xiangqing: 'Связь глобальных сообществ открытой науки' },
      { biaoti: 'Политика и стандарты', xiangqing: 'Отслеживание политики открытой науки и стандартов данных' }
    ],
    footer: {
      brandName: 'Open Explore',
      institutionName: 'Платформа поиска ресурсов открытой науки',
      address: 'Чжунгуаньцунь, район Хайдянь, Пекин',
      email: 'contact@open-explore.org',
      copyright: '© 2026 Open Explore'
    }
  },
  footer: {
    contactUs: 'Свяжитесь с нами',
    copyright: '© 2026 OpenScience¹⁰. Все права защищены.'
  },
  about: {
    title: 'Об Open Explore',
    subtitle: 'Платформа с открытым исходным кодом для поиска ресурсов открытой науки.',
    introduction: [
      'Open Explore — это полноценная платформа для поиска ресурсов в экосистеме открытой науки. Она объединяет четыре типа ресурсов: академическую литературу, научные данные, научное ПО и крупные исследовательские установки.',
      'Проект построен на Elasticsearch, с фронтендом на Vue.js и бэкендом на Spring Boot. Поддерживает многоязычный интерфейс (китайский, английский, испанский, французский, русский) с фасетным поиском и агрегированной статистикой.',
      'Наша цель — снизить барьер доступа к исследовательским ресурсам и способствовать глобальному прогрессу открытой науки.'
    ],
    contributorsTitle: 'Участники',
    contributors: [
      { name: 'Чжан Сань', avatar: 'https://img0.baidu.com/it/u=4279917707,1303528668&fm=253&app=138&f=JPEG?w=500&h=500' },
      { name: 'Ли Сы', avatar: 'https://img0.baidu.com/it/u=4279917707,1303528668&fm=253&app=138&f=JPEG?w=500&h=500' },
      { name: 'Ван У', avatar: 'https://img0.baidu.com/it/u=4279917707,1303528668&fm=253&app=138&f=JPEG?w=500&h=500' },
      { name: 'Чжао Лю', avatar: 'https://img0.baidu.com/it/u=4279917707,1303528668&fm=253&app=138&f=JPEG?w=500&h=500' },
      { name: 'Чэнь Ци', avatar: 'https://img0.baidu.com/it/u=4279917707,1303528668&fm=253&app=138&f=JPEG?w=500&h=500' },
      { name: 'Лю Ба', avatar: 'https://img0.baidu.com/it/u=4279917707,1303528668&fm=253&app=138&f=JPEG?w=500&h=500' }
    ],
    footer: '© 2026 Open Explore. Открытая наука, обмен знаниями.'
  }
}
