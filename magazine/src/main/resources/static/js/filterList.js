

var myFilter = new Vue({
  el: '#filter',
  data: {
    filters: []
  }
});

var responseData;

axios.get('/filters')
    .then(function(response) {
        console.log(response);
        responseData = response.data;

        myFilter.filters = responseData;
    })
    .catch(function(error) {
        console.log(error);
    });