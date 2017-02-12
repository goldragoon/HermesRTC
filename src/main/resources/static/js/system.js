'use strict';

var System = new function () {
	this.CREATE = 'create'
	this.READ = 'read'
	this.UPDATE = 'update'
	this.DELETE = 'delete'

	// route
	this.DEVELOPER = 'developer'
	this.SIGNAL = 'signal'
	this.KEY = 'key'

	this.getURL = function (route, operation, data) {
		return '/' + route + '/' + operation + (data.url_param ? '/' + data.url_param : '')
	}

	this.execute = function (route, operation, data, successCallback, errorCallback) {
		$.ajax({
			type: 'POST',
			url: this.getURL(route, operation, data),
			contentType: 'application/json',
			dataType: 'json',
			data: JSON.stringify(data),
			success: successCallback,
			error: errorCallback
		})
	}

	this.serializeArrayToDic = function (array)
	{
		var dic = {}
		var element = "";
		for (element in array)
		{
			dic[array[element].name] = array[element].value
		}
		console.log(dic)
		return dic
	}

	this.executeForm = function (route, operation, data, form, successCallback, errorCallback) {
		form.ajaxForm({
			type: 'POST',
			url: this.getURL(route, operation, data),
			contentType: 'application/json',
			dataType: 'json',
			success: successCallback,
			error: errorCallback
		})
	}
}