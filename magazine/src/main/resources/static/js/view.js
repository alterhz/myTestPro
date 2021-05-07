
/**
* 搜索
*/
var mySearch = new Vue({
    el: '#mySearch',
    data: {
        loading: true,
        searchText: '',
        searchField: '',    //默认搜索字段
        sortField: '',  //排序字段
        sortAsc: '升序',  //升序或者降序
        keys: {},   //检索字段的key
        showMore: false,    //是否显示多字段搜索
        showContact: true,  //是否显示联系方式
        fields: [], //字段列表
        orgRows: [],    //原始行数据
        rows: []   //检索后的数据
    },
    watch: {
        sortField: function(sortField, oldSortField) {
//            console.log("sortField from =" + oldSortField + ", to =" + sortField);
            this.refreshSort();
        },
        sortAsc: function(sortAsc, oldSortAsc) {
//            console.log("sortAsc from =" + oldSortAsc + ", to =" + sortAsc);
            this.refreshSort();
        }
    },
    methods: {
        search: function() {
//            console.log(this.searchText);
            // concat复制行数数据
            var filterRows = this.orgRows.concat();
            if (this.showMore) {
                // 多字段检索
                this.rows = filterRows.filter(row => {
                    for (var key in this.keys) {
                        if (row[key].indexOf(this.keys[key]) === -1) {
                            return false;
                        }
                    }
                    return true;
                });
            } else {
                // 默认单字段检索
                console.log("单字段检索.this.searchText = " + this.searchText);
                this.rows = filterRows.filter(row => row[this.searchField].indexOf(this.searchText) !== -1);
            }
        },
        refreshSort: function() {
            let sortAsc = this.sortAsc;
            let sortField = this.sortField;
            console.log('refresh sort. this.sortField = ' + this.sortField + ", this.sortAsc = " + this.sortAsc);
            let sortType = 0;
            for (let key in this.fields) {
                if (this.fields[key].field === this.sortField) {
                    sortType = this.fields[key].sortType;
                    console.log("sortType = " + sortType);
                    break;
                }
            }
            this.rows.sort(function(a, b) {
                        if (a[sortField] !== undefined && b[sortField] !== undefined) {
                            if (sortType == 1) {
                                // id列特殊处理
                                if (sortAsc === '升序') {
                                    return parseInt(a[sortField]) - parseInt(b[sortField]);
                                } else {
                                    return parseInt(b[sortField]) - parseInt(a[sortField]);
                                }
                            } else {
                                if (sortAsc === '升序') {
                                    return a[sortField].localeCompare(b[sortField], 'zh-Hans-CN', {sensitivity: 'accent'});
                                } else {
                                    return b[sortField].localeCompare(a[sortField], 'zh-Hans-CN', {sensitivity: 'accent'});
                                }
                            }
                        } else {
                            console.log("sortField is error." + sortField);
                            return true;
                        }
                    });
        }
    }
});



// 获取url参数
function getQueryString(name) {
  let reg = `(^|&)${name}=([^&]*)(&|$)`
  let r = window.location.search.substr(1).match(reg);
  if (r != null) return unescape(r[2]); return null;
}

// 获取url参数中的过滤器名称
var filter = getQueryString("filter");
console.log(filter);
axios.get('/view?filter=' + filter)
    .then(function(response) {
        mySearch.loading = false;
        console.log(response);
        var mySheet = response.data;

        var sortField = mySheet.sortField;
        // 按照搜索键进行排序
        sortRows = mySheet.rows.concat();
        if (sortField !== undefined && sortField.length > 0) {
            sortRows.sort(function(a, b) {
                // {sensitivity: 'base'}
                if (a[sortField] !== undefined && b[sortField] !== undefined) {
                    return a[sortField].localeCompare(b[sortField], 'zh-Hans-CN', {sensitivity: 'accent'});
                } else {
                    return true;
                }
            });

            console.debug(sortRows);
        }

        // 只需要做一次替换
        sortRows.forEach(row => {
           for (var key in row) {
               var url = row[key];
               if (url.indexOf('http') !== -1) {
                   row[key] = '<a href="' + url + '" target="_blank">' + url + '</a> <button class="btn badge" data-clipboard-text="' + url + '">复制</button>';
                   console.debug("replace" + url);
               }
           }
        });


        // 默认搜索字段
        mySearch.searchField = mySheet.searchField;
        // 默认排序字段
        mySearch.sortField = mySheet.sortField;
        // 是否显示联系方式
        mySearch.showContact = mySheet.sheetFilter.showContact;
        // 字段数据
        mySearch.fields = mySheet.sheetFilter.fields;
        // 原始数据，每次搜索都使用原始数据过滤
        mySearch.orgRows = sortRows;
        mySearch.rows = mySearch.orgRows.concat();

        runClipboard();
    })
    .catch(function(error) {
        console.log(error);
    });

    function runClipboard() {
        var clipboard = new ClipboardJS('.btn');

        clipboard.on('success', function(e) {
            console.debug('Action:', e.action);
            console.debug('Text:', e.text);
            console.debug('Trigger:', e.trigger);

            e.clearSelection();

            alert("网址[" + e.text + "]已复制到剪切板");
        });

        clipboard.on('error', function(e) {
            console.error('Action:', e.action);
            console.error('Trigger:', e.trigger);
        });
    }