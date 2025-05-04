'use strict';

/**
 * User request profile controller.
 */
angular.module('docs').controller('UserRequestProfile', function($stateParams, Restangular, $scope) {
  // Load user
  Restangular.one('user/request', $stateParams.username).get().then(function(data) {
    $scope.userRequest = data;
  });
});