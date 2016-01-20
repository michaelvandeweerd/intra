<!DOCTYPE html>
<html lang="%lang%">
	<head>
		<title>parcif.al</title>
		<!-- meta -->
		<meta charset="UTF-8">
		<meta name="description" content="%string(pagina/kort/string.sp, description)%">
		<meta name="keywords" content="Parcifal Programmatuur">
		<!-- links -->
		<link rel="icon" type="image/png" href="http://file.parcifal.eu.localhost/favicon.png">
		<link rel="stylesheet" type="text/css" href="http://file.parcifal.eu.localhost/kort/index.css">
		<!-- scripts -->
		<script async src="http://file.parcifal.eu.localhost/cargo.js"></script>
		<script src="http://file.parcifal.eu.localhost/kort/index.js"></script>
	</head>
	<body>
		<header>
			%load(pagina/kort/header.pp)%
		</header>
		<main>
			<h2>%string(pagina/kort/string.sp, h2-1)%</h2>
			<p>%string(pagina/kort/string.sp, par-1)%</p>
			<h2>%string(pagina/kort/string.sp, h2-2)%</h2>
			<p>%string(pagina/kort/string.sp, par-2)%</p>
			<noscript>
				<p class="warning">%string(pagina/global.sp, warning-enable-javascript)%</p>
			</noscript>
			<form class="kort">
				<input type="url" name="reference" placeholder="%string(pagina/kort/string.sp, inp-ph-1)%">
				<label><input type="checkbox" name="is-custom">%string(pagina/kort/string.sp, lbl-1)%</label>
				<input type="text" maxlength="8" name="identifier" placeholder="%string(pagina/kort/string.sp, inp-ph-2)%">
				<input type="submit" name="submit" value="%string(pagina/global.sp, submit)%">
			</form>
		</main>
		<footer>
			%load(pagina/kort/footer.pp)%
		</footer>
	</body>
</html>