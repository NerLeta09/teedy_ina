'use strict';

/**
 * User/group controller.
 */
angular.module('docs').controller('UserGroup', function(Restangular, $scope, $state) {
  // Load users
  Restangular.one('user/list').get({
    sort_column: 1,
    asc: true
  }).then(function(data) {
    $scope.users = data.users;
  });

  // Load groups
  Restangular.one('group').get({
    sort_column: 1,
    asc: true
  }).then(function(data) {
    $scope.groups = data.groups;
  });

  // Load user requests
  Restangular.one('user/request').get().then(function(data) {
    $scope.userRequests = data.userRequests;
  });

  // Open a user
  $scope.openUser = function(user) {
    $state.go('user.profile', { username: user.username });
  };

  // Open a group
  $scope.openGroup = function(group) {
    $state.go('group.profile', { name: group.name });
  };

  

  // Open a user request
  $scope.openUserRequest = function(userRequest) {
    $state.go('user.request.profile', { id: userRequest.id });
  };
});