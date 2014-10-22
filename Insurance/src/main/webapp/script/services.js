angular.module('insureds.services', []).
 factory('eventsAPIService', function($http) {
	 var eventsAPI = {};
           eventsAPI.getEvents  = function() {
           var serviceURL = "/brmsGUI";
           var response = $http({
                 method: 'JSONP',
                 url: serviceURL
           });
           return response;
   };
   return eventsAPI;
 });