import moment from 'moment'

/**
 * 日期快捷选项适用于 el-date-picker
 */
export const defaultShortcuts = [
  {
    text: '今天',
    value: () => {
      return new Date()
    }
  },
  {
    text: '昨天',
    value: () => {
      const date = new Date()
      date.setTime(date.getTime() - 3600 * 1000 * 24)
      return [date, date]
    }
  },
  {
    text: '最近七天',
    value: () => {
      const date = new Date()
      date.setTime(date.getTime() - 3600 * 1000 * 24 * 7)
      return [date, new Date()]
    }
  },
  {
    text: '最近 30 天',
    value: () => {
      const date = new Date()
      date.setTime(date.getTime() - 3600 * 1000 * 24 * 30)
      return [date, new Date()]
    }
  },
  {
    text: '本月',
    value: () => {
      const date = new Date()
      date.setDate(1) // 设置为当前月的第一天
      return [date, new Date()]
    }
  },
  {
    text: '今年',
    value: () => {
      const date = new Date()
      return [new Date(`${date.getFullYear()}-01-01`), date]
    }
  }
]

/**
 * element plus 的时间 Formatter 实现，使用 YYYY-MM-DD HH:mm:ss 格式
 *
 * @param row 行数据
 * @param column 字段
 * @param cellValue 字段值
 */
export function dateFormatter (_row, _column, cellValue) {
  return cellValue ? formatDate(cellValue) : ''
}

/**
 * 时间日期转换
 * @param date 当前时间，new Date() 格式
 * @param format 需要转换的时间格式字符串
 * @description format 字符串随意，如 `YYYY-mm、YYYY-mm-dd`
 * @description format 季度："YYYY-mm-dd HH:MM:SS QQQQ"
 * @description format 星期："YYYY-mm-dd HH:MM:SS WWW"
 * @description format 几周："YYYY-mm-dd HH:MM:SS ZZZ"
 * @description format 季度 + 星期 + 几周："YYYY-mm-dd HH:MM:SS WWW QQQQ ZZZ"
 * @returns 返回拼接后的时间字符串
 */
export function formatDate (date, format) {
  console.log('formatDate', date, format)
  // 日期不存在，则返回空
  if (!date) {
    return ''
  }
  // 日期存在，则进行格式化
  return date ? moment(date).format(format ? 'YYYY-MM-DD HH:mm:ss' : '') : ''
}

/**
 * 获取当前的日期+时间
 */
export function getNowDateTime () {
  return moment()
}

/**
 * 获取当前日期是第几周
 * @param dateTime 当前传入的日期值
 * @returns 返回第几周数字值
 */
export function getWeek (dateTime) {
  const temptTime = new Date(dateTime.getTime())
  // 周几
  const weekday = temptTime.getDay() || 7
  // 周1+5天=周六
  temptTime.setDate(temptTime.getDate() - weekday + 1 + 5)
  let firstDay = new Date(temptTime.getFullYear(), 0, 1)
  const dayOfWeek = firstDay.getDay()
  let spendDay = 1
  if (dayOfWeek != 0) spendDay = 7 - dayOfWeek + 1
  firstDay = new Date(temptTime.getFullYear(), 0, 1 + spendDay)
  const d = Math.ceil((temptTime.valueOf() - firstDay.valueOf()) / 86400000)
  return Math.ceil(d / 7)
}

/**
 * 将时间转换为 `几秒前`、`几分钟前`、`几小时前`、`几天前`
 * @param param 当前时间，new Date() 格式或者字符串时间格式
 * @param format 需要转换的时间格式字符串
 * @description param 10秒：  10 * 1000
 * @description param 1分：   60 * 1000
 * @description param 1小时： 60 * 60 * 1000
 * @description param 24小时：60 * 60 * 24 * 1000
 * @description param 3天：   60 * 60* 24 * 1000 * 3
 * @returns 返回拼接后的时间字符串
 */
export function formatPast (param, format = 'YYYY-mm-dd HH:MM:SS') {
  // 传入格式处理、存储转换值
  let t, s
  // 获取js 时间戳
  let time = new Date().getTime()
  // 是否是对象
  typeof param === 'string' || 'object' ? (t = new Date(param).getTime()) : (t = param)
  // 当前时间戳 - 传入时间戳
  time = Number.parseInt(`${time - t}`)
  if (time < 10000) {
    // 10秒内
    return '刚刚'
  } else if (time < 60000 && time >= 10000) {
    // 超过10秒少于1分钟内
    s = Math.floor(time / 1000)
    return `${s}秒前`
  } else if (time < 3600000 && time >= 60000) {
    // 超过1分钟少于1小时
    s = Math.floor(time / 60000)
    return `${s}分钟前`
  } else if (time < 86400000 && time >= 3600000) {
    // 超过1小时少于24小时
    s = Math.floor(time / 3600000)
    return `${s}小时前`
  } else if (time < 259200000 && time >= 86400000) {
    // 超过1天少于3天内
    s = Math.floor(time / 86400000)
    return `${s}天前`
  } else {
    // 超过3天
    const date = typeof param === 'string' || 'object' ? new Date(param) : param
    return formatDate(date, format)
  }
}

/**
 * 时间问候语
 * @param param 当前时间，new Date() 格式
 * @description param 调用 `formatAxis(new Date())` 输出 `上午好`
 * @returns 返回拼接后的时间字符串
 */
export function formatAxis (param) {
  const hour = new Date(param).getHours()
  if (hour < 6) return '凌晨好'
  else if (hour < 9) return '早上好'
  else if (hour < 12) return '上午好'
  else if (hour < 14) return '中午好'
  else if (hour < 17) return '下午好'
  else if (hour < 19) return '傍晚好'
  else if (hour < 22) return '晚上好'
  else return '夜里好'
}

/**
 * 将毫秒，转换成时间字符串。例如说，xx 分钟
 *
 * @param ms 毫秒
 * @returns {string} 字符串
 */
export function formatPast2 (ms) {
  const day = Math.floor(ms / (24 * 60 * 60 * 1000))
  const hour = Math.floor(ms / (60 * 60 * 1000) - day * 24)
  const minute = Math.floor(ms / (60 * 1000) - day * 24 * 60 - hour * 60)
  const second = Math.floor(ms / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60)
  if (day > 0) {
    return day + ' 天' + hour + ' 小时 ' + minute + ' 分钟'
  }
  if (hour > 0) {
    return hour + ' 小时 ' + minute + ' 分钟'
  }
  if (minute > 0) {
    return minute + ' 分钟'
  }
  if (second > 0) {
    return second + ' 秒'
  } else {
    return 0 + ' 秒'
  }
}

/**
 * 设置起始日期，时间为00:00:00
 * @param param 传入日期
 * @returns 带时间00:00:00的日期
 */
export function beginOfDay (param) {
  return new Date(param.getFullYear(), param.getMonth(), param.getDate(), 0, 0, 0)
}

/**
 * 设置结束日期，时间为23:59:59
 * @param param 传入日期
 * @returns 带时间23:59:59的日期
 */
export function endOfDay (param) {
  return new Date(param.getFullYear(), param.getMonth(), param.getDate(), 23, 59, 59)
}

/**
 * 计算两个日期间隔天数
 * @param param1 日期1
 * @param param2 日期2
 */
export function betweenDay (param1, param2) {
  param1 = convertDate(param1)
  param2 = convertDate(param2)
  // 计算差值
  return Math.floor((param2.getTime() - param1.getTime()) / (24 * 3600 * 1000))
}

/**
 * 日期计算
 * @param param1 日期
 * @param param2 添加的时间
 */
export function addTime (param1, param2) {
  param1 = convertDate(param1)
  return new Date(param1.getTime() + param2)
}

/**
 * 日期转换
 * @param param 日期
 */
export function convertDate (param) {
  if (typeof param === 'string') {
    return new Date(param)
  }
  return param
}

/**
 * 指定的两个日期, 是否为同一天
 * @param a 日期 A
 * @param b 日期 B
 */
export function isSameDay (a, b) {
  if (!a || !b) return false

  const aa = moment(a)
  const bb = moment(b)
  return aa.year() == bb.year() && aa.month() == bb.month() && aa.day() == bb.day()
}

/**
 * 获取一天的开始时间、截止时间
 * @param date 日期
 * @param days 天数
 */
export function getDayRange (
  date,
  days
) {
  const day = moment(date).add(days, 'd')
  return getDateRange(day, day)
}

/**
 * 获取最近7天的开始时间、截止时间
 */
export function getLast7Days () {
  const lastWeekDay = moment().subtract(7, 'd')
  const yesterday = moment().subtract(1, 'd')
  return getDateRange(lastWeekDay, yesterday)
}

/**
 * 获取最近30天的开始时间、截止时间
 */
export function getLast30Days () {
  const lastMonthDay = moment().subtract(30, 'd')
  const yesterday = moment().subtract(1, 'd')
  return getDateRange(lastMonthDay, yesterday)
}

/**
 * 获取最近1年的开始时间、截止时间
 */
export function getLast1Year () {
  const lastYearDay = moment().subtract(1, 'y')
  const yesterday = moment().subtract(1, 'd')
  return getDateRange(lastYearDay, yesterday)
}

/**
 * 获取指定日期的开始时间、截止时间
 * @param beginDate 开始日期
 * @param endDate 截止日期
 */
export function getDateRange (
  beginDate,
  endDate
) {
  return [
    moment(beginDate).startOf('d').format('YYYY-MM-DD HH:mm:ss'),
    moment(endDate).endOf('d').format('YYYY-MM-DD HH:mm:ss')
  ]
}

/**
 * 时间格式化 (昨天、今天)
 */
export function timestampFrom (timestamp) {
  const date = new Date(timestamp)
  const nowYear = new Date().getFullYear()
  const year = date.getFullYear() // 年
  const month = (date.getMonth() + 1).toString().padStart(2, '0') // 月份补零
  const day = date.getDate().toString().padStart(2, '0') // 日补零
  const hours = date.getHours().toString().padStart(2, '0') // 获取小时并补零
  const minutes = date.getMinutes().toString().padStart(2, '0') // 获取分钟并补零
  const lastNow = new Date()
  lastNow.setHours(0)
  lastNow.setMinutes(0)
  lastNow.setSeconds(0)
  const yesterdayTime = new Date(lastNow - 24 * 60 * 60 * 1000) // 昨天凌晨的时间
  // 今天： 00点 - 24点
  const currMonth = (lastNow.getMonth() + 1).toString().padStart(2, '0')
  const currDay = lastNow.getDate().toString().padStart(2, '0')
  const day00 = new Date(`${nowYear}/${currMonth}/${currDay} 00:00:00`)
  const day24 = new Date(`${nowYear}/${currMonth}/${currDay} 23:59:59`)
  // 先判断是否今天
  if (date.getTime() > day00.getTime() && date.getTime() < day24.getTime()) {
    return `今天  ${hours}:${minutes}`
  } else if (date.getTime() < lastNow.getTime() && yesterdayTime.getTime() <= date.getTime()) {
    return `昨天  ${hours}:${minutes}`
  } else {
    return `${year}-${month}-${day} ${hours}:${minutes}`
  }
}
