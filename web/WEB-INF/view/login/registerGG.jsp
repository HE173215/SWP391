<form action="/submit_registration" method="POST">
    <input type="email" name="email" id="email" required />
    <!-- C�c tr??ng kh�c -->
    <button type="submit">??ng k�</button>
</form>

<script>
    const urlParams = new URLSearchParams(window.location.search);
    const email = urlParams.get('email');
    if (email) {
        document.getElementById('email').value = email;
    }
</script>
