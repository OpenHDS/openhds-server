'use strict';


// Declare app level module which depends on filters, and services
angular.module('tabletuing', [
  'ngRoute',
  'tabletuing.filters',
  'tabletuing.services',
  'tabletuing.directives',
  'tabletuing.controllers'
]).
config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/form/location', {templateUrl: 'partials/form/location.html', controller: 'locationCtrl'});
  $routeProvider.when('/view1', {templateUrl: 'partials/partial1.html', controller: 'MyCtrl1'});
  $routeProvider.when('/view2', {templateUrl: 'partials/partial2.html', controller: 'MyCtrl2'});
  $routeProvider.when('/home', {templateUrl: 'partials/home.html', controller: 'HomeCtrl'});
  $routeProvider.otherwise({redirectTo: '/home'});
}]);
