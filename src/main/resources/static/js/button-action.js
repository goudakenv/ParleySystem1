document.querySelectorAll('.delete')
	.forEach(button =>  {
		button.addEventListener('click', (e) => {
			if(!confirm('本当に削除してよろしいですか？')) {
				e.preventDefault();
			}
		});
	});
