//计算正态曲线
export function fxNormalDistribution(array, std, mean) {
    let valueList = [];
    for (let i = 0; i < array.length; i++) {
      let ND =
        Math.sqrt(2 * Math.PI) *
        std *
        Math.pow(
          Math.E,
          -(Math.pow(array[i] - mean, 2) / (2 * Math.pow(std, 2)))
        );
      valueList.push(ND.toFixed(3));
    }
    return valueList;
  }
  
  //计算标准差
  export function getStd(data, mean) {
    let sumXY = function (x, y) {
      return Number(x) + Number(y);
    };
    let square = function (x) {
      return Number(x) * Number(x);
    };
    let deviations = data.map(function (x) {
      return x - mean;
    });
    return Math.sqrt(deviations.map(square).reduce(sumXY) / (data.length - 1));
  }
  
  
  //对有序数组求中位数
  export function getMedianSorted(arr) {
    // 获取数组长度
    let len = arr.length;
  
    // 如果没有元素，返回undefined或你可以返回其他合适的值
    if (len === 0) {
      return undefined;
    }
  
    // 如果只有一个元素，那么它就是中位数
    if (len === 1) {
      return arr[0];
    }
  
    // 如果数组长度是奇数，返回中间的数
    if (len % 2 === 1) {
      return arr[Math.floor(len / 2)];
    }
  
    // 如果数组长度是偶数，返回中间两个数的平均值
    else {
      let mid1 = arr[len / 2 - 1];
      let mid2 = arr[len / 2];
      return (mid1 + mid2) / 2.0;
    }
  }
  