<form action="/submit_registration" method="POST">
    <input type="email" name="email" id="email" required />
    <!-- Các tr??ng khác -->
    <button type="submit">??ng ký</button>
</form>

<script>
    const urlParams = new URLSearchParams(window.location.search);
    const email = urlParams.get('email');
    if (email) {
        document.getElementById('email').value = email;
    }
</script>
