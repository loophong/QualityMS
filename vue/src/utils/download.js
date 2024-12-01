// import JSZip from 'jszip'
// import FileSaver from 'file-saver'
import axios from 'axios'
import { MessageBox } from 'element-ui'

const download0 = (data, fileName, mimeType) => {
  // 创建 blob
  const blob = mimeType ? new Blob([data], { type: mimeType }) : new Blob([data])
  // 创建 href 超链接，点击进行下载
  window.URL = window.URL || window.webkitURL
  const href = URL.createObjectURL(blob)
  const downA = document.createElement('a')
  downA.href = href
  downA.download = fileName
  downA.click()
  // 销毁超连接
  window.URL.revokeObjectURL(href)
}

/**
 * 通过url下载文件并对下载的文件更名
 * @param {*} url
 * @param {*} filename
 */
export const downloadUrl = (url, filename) => {
  const fileName = getFileName(filename, url)
  getFile(url).then((data) => {
    download0(data, fileName)
  }).catch((error) => {
    console.log(error, '文件未找到----')
    MessageBox({
      showClose: true,
      message: '文件未找到！',
      type: 'error'
    })
  })
}

/**
 * 批量下载文件
 * @param files name: 文件名, url: 文件路径, folder: 文件夹名
 * @param downloadName
 */
const batchDownloadUrl = (files, downloadName) => {
  // const zip = new JSZip()
  // const promises = []
  // files.forEach((item) => {
  //   const promise = getFile(item.url).then((data) => {
  //     // 下载文件, 并存成ArrayBuffer对象
  //     const fileName = getFileName(item.name, item.url)
  //     if (item.folder) {
  //       const folder = zip.folder(item.folder)
  //       folder && folder.file(fileName, data, { binary: true }) // 逐个添加文件
  //     } else {
  //       zip.file(fileName, data, { binary: true }) // 逐个添加文件
  //     }
  //   })
  //   promises.push(promise)
  // })
  // Promise.all(promises).then(() => {
  //   zip.generateAsync({ type: 'blob' }).then((content) => {
  //     FileSaver.saveAs(content, downloadName + '.zip') // 利用file-saver保存文件
  //   })
  // }).catch((error) => {
  //   console.log(error, '文件压缩失败----')
  //   // error.config.url  下载失败的路径
  //   files.map((v) => {
  //     if (v.url == error.config.url) {
  //       ElMessage({
  //         showClose: true,
  //         message: '名称：' + v.name + '的' + '的文件未找到！',
  //         type: 'error'
  //       })
  //     }
  //   })
  // })
}

const getFileName = (name, url) => {
  const fileSuffix = getFileExtension(name) // 获取文件后缀
  if (!fileSuffix) {
    return name + getFileExtension(url) // 如果文件名没有后缀，就用url的后缀
  }
  return name
}

const getFileExtension = (url) => {
  const match = url.match(/(\.[^./]+)$/)
  return match ? match[1] : ''
}

const getFile = (url) => {
  return new Promise((resolve, reject) => {
    axios({
      method: 'get',
      url,
      responseType: 'arraybuffer'
    }).then((data) => {
      resolve(data.data)
    }).catch((error) => {
      console.log(error, 'error')
      ElMessage({
        showClose: true,
        message: error.response.request.responseURL + ' 文件未找到！',
        type: 'error'
      })
      reject(error.toString())
    })
  })
}

const download = {
  // 下载 Excel 方法
  excel: (data, fileName) => {
    download0(data, fileName, 'application/vnd.ms-excel')
  },
  // 下载 Word 方法
  word: (data, fileName) => {
    download0(data, fileName, 'application/msword')
  },
  // 下载 Zip 方法
  zip: (data, fileName) => {
    download0(data, fileName, 'application/zip')
  },
  // 下载 Html 方法
  html: (data, fileName) => {
    download0(data, fileName, 'text/html')
  },
  // 下载 Markdown 方法
  markdown: (data, fileName) => {
    download0(data, fileName, 'text/markdown')
  },
  // 批量下载 Zip 方法（通过 url 方式）
  batchUrlZip: (files, fileName = '批量下载') => {
    batchDownloadUrl(files, fileName)
  },
  // 通过 url 下载
  url: (url, fileName) => {
    downloadUrl(url, fileName)
  }
}

export default download
