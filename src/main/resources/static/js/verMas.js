// Cantidad inicial de huertas y cultivos visibles -->

////  En este código, hemos separado el botón "Más" en dos botones diferentes: uno para cargar más
// huertas (btn-cargar-mas-huertas) y otro para cargar más cultivos (btn-cargar-mas-cultivos). 
// También hemos adaptado la cantidad de elementos a mostrar y aumentar para que funcione con huertas y cultivos.

///  Asegúrate de que los botones "Más Huertas" y "Más Cultivos" en el HTML tengan los identificadores 
//correctos para que el JavaScript los seleccione correctamente.

///Recuerda que este código asume que ya tienes los datos de huertas y cultivos en el HTML, y que estás utilizando 
//las clases CSS .huertas-list y .cultivos-list para representar cada elemento de la lista.
// Si no tienes esa estructura, deberás ajustar el código para que se adapte a tu HTML.




var cantidadInicial = 3;

// Obtener todas las huertas y ocultar las que excedan la cantidad inicial
var huertas = document.querySelectorAll('.huertas-list');
for (var i = cantidadInicial; i < huertas.length; i++) {
  huertas[i].style.display = 'none';
}

// Obtener todos los cultivos y ocultar los que excedan la cantidad inicial
var cultivos = document.querySelectorAll('.cultivos-list');
for (var i = cantidadInicial; i < cultivos.length; i++) {
  cultivos[i].style.display = 'none';
}

// Manejar el evento clic del botón "Más Huertas"
var btnCargarMasHuertas = document.getElementById('btn-cargar-mas-huertas');
btnCargarMasHuertas.addEventListener('click', function() {
  // Mostrar las siguientes huertas ocultas
  for (var i = cantidadInicial; i < cantidadInicial + 4 && i < huertas.length; i++) {
    huertas[i].style.display = 'block';
  }
  // Incrementar la cantidad inicial para la próxima carga de huertas
  cantidadInicial += 4;
  // Ocultar el botón si no quedan más huertas por mostrar
  if (cantidadInicial >= huertas.length) {
    btnCargarMasHuertas.style.display = 'none';
  }
});

// Manejar el evento clic del botón "Más Cultivos"
var btnCargarMasCultivos = document.getElementById('btn-cargar-mas-cultivos');
btnCargarMasCultivos.addEventListener('click', function() {
  // Mostrar los siguientes cultivos ocultos
  for (var i = cantidadInicial; i < cantidadInicial + 4 && i < cultivos.length; i++) {
    cultivos[i].style.display = 'block';
  }
  // Incrementar la cantidad inicial para la próxima carga de cultivos
  cantidadInicial += 4;
  // Ocultar el botón si no quedan más cultivos por mostrar
  if (cantidadInicial >= cultivos.length) {
    btnCargarMasCultivos.style.display = 'none';
  }
});

