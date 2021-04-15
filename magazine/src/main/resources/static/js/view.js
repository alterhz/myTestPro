
var mySheet = new Vue({
    el: '#sheet',
    data: {
        rows: [],
        fields: []
    }
});

// 原始数据
var responseData;

axios.get('/view?filter=bookFilter')
    .then(function(response) {
        console.log(response);
        console.log(response.data);
        responseData = response.data;

        mySheet.fields = responseData.fields;
        mySheet.rows = responseData.rows;
    })
    .catch(function(error) {
        console.log(error);
    });

//axios.get('/view', {
//    param: {
//        filter: 'bookFilter'
//        }
//    })
//    .then(function(response) {
//        console.log(response);
//    })
//    .catch(function(error) {
//        console.log(error);
//    });