export default function(uri, target) {
	if(!(typeof uri === "string" && (uri + "").trim().length > 0)) {
		uri = "/";
	}
	if(!(["_blank", "_parent", "_search", "_self", "_top"].indexOf(target) > -1)) {
		target = "_self";
	}
	var id = "redirect-generator-" + Math.random();
	var el = document.createElement("a");
	el.setAttribute("id", id);
	el.setAttribute("href", uri);
	el.setAttribute("target", target);
	document.body.appendChild(el);
	document.getElementById(id).click();
	setTimeout(function() {
		document.getElementById(id).remove();
	}, 800);
};