	let maxDate = new Date();
	console.log(maxDate);
	maxDate = maxDate.setMonth(maxDate.getMonth() + 3);
	
	flatpickr('#fromCheckinDateToCheckoutDate',{
		mode : "range",
		locale : 'ja',
		minDate : 'today',
		maxDate : maxDate
	});