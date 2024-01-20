$(function() {
	$('#imageFile').on('change', function() {
		if (this.files[0]) {
			let fileReader = new FileReader();
			fileReader.onload = () => {
				$('#imagePreview').html(`<img src="${fileReader.result}" class="mb-3">`);
			}
			fileReader.readAsDataURL(this.files[0]);
		}
		else {
			$('#imagePreview').html('');
		}
	});
});