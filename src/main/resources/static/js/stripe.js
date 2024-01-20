const stripe = Stripe('pk_test_51OTfSjKzUKO9F9ZMr5OMozPZNnVpjpq95S5IjO5r6h2lLZIe0L5lNskY70GHMr685UrEYHj7kzBVuwcKdMnKN2a300jlQhMiRb');
const paymentButton = document.querySelector('#paymentButton');
console.log(stripe);
paymentButton.addEventListener('click', () => {
	stripe.redirectToCheckout({
		sessionId: sessionId
	})
});