'use strict';

/* Controllers */

angular.module('tabletuing.controllers', []).
   controller('MainCtrl', ['$scope', '$location', function ($scope, $location) {
	   $scope.go = function ( path ) {
		   $location.path( path );
	    };
   }])
  .controller('MyCtrl1', [function() {

  }])
  .controller('MyCtrl2', [function() {

  }])
  .controller('locationCtrl', ['$scope', '$resource', function ($scope, $resource) {
	        $scope.master = {};
	   
//	        $scope.createLocation = function() {
//	          $scope.master = angular.copy($scope.location);
//	        };
	   
	        $scope.reset = function() {
	          $scope.location = angular.copy($scope.master);
	        };
	   
	        $scope.resetAll = function() {
	          $scope.master = {};
	          $scope.location = angular.copy($scope.master);
	          $scope.locationForm.$setPristine();
	        };
	        
	        $scope.reset();
	        
	        var locationResource = $resource('/openhds2/api/rest/locations/:extId');
	        
	        $scope.getLocations = function() {
	        	locationResource.get({}, function(locationResource) {
	        		$scope.locations = locationResource;
	        		console.log("Get locations " + locationResource);
	        	});
	        	
	        };
	        
	        $scope.getLocation = function(id) {
	        	locationResource.get({extId:id}, function(locationResource) {
	        		$scope.location = locationResource;
	        		console.log("Get locations " + locationResource);
	        	});
	        	console.log("Get location with id " + id);
	        };
	        
	        $scope.createLocation = function() {
	        	locationResource.save($scope.location, function(locationResource) {
	        		console.log("Location created");
	        	});
	        };
  }])
  .controller('HomeCtrl', [function() {

  }])
  ;