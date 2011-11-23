/**
 * Loads the special study into an iframe
 */
(function() {
	var specialStudyLoginPage = '/specialStudyLogin';
	var pageUrl, sessId, specialStudyLoc;
	var listener;
	
	function loadSpecialStudy(specialStudyLocation, sessionId, pageToView, theListener) {
		pageUrl = pageToView;
		sessId = sessionId;
		specialStudyLoc = specialStudyLocation;
		listener = theListener;
		
		$.ajax({
			url: specialStudyLocation,
			complete: completeCallback,
		});	
	}
	
	function completeCallback(xhr, textStatus) {
		if (xhr.status === 200 || xhr.status === 302) {
			$('#specialStudyFrame').bind('load', loadingSpecialStudyComplete);
			$('#specialStudyFrame').attr('src', buildUrl());
		} else {
			$('#specialStudySpan').html('Cannot load Household Characteristics');
			$('#specialStudyFrame').height(50);
			if (listener && listener.onLoadFailure) {
				listener.onLoadFailure();
			}
		}
	}
	
	function buildUrl() {
		var target = specialStudyLoc + specialStudyLoginPage + '?openhdsSessionId=' + sessId + '&targetUrl=' + pageUrl; 
		
		return target;
	}
	
	function loadingSpecialStudyComplete() {
		var iframe = $('#specialStudyFrame').get(0);
	
		// resetting the height is required because on post back
		// (when user hits save) the details page is not as tall
		// as the create page. If the height is not reset, it will
		// always return the height set on the initial page load
		$('#specialStudyFrame').css('height', '');
		
		var iframeHeight = $(iframe.contentDocument).height();
		$('#specialStudyFrame').css('height', iframeHeight + 'px');
	
		$('#specialStudySpan').remove();
		$('#specialStudyFrame').css('visibility', 'visible');
	
		$(iframe.contentDocument).find('input').first().focus();
	}
	
	window.loadSpecialStudy = loadSpecialStudy;
})();