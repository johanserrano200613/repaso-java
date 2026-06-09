const problemas = [
  problema1,
  problema2,
  problema3,
  problema4,
  problema5,
  problema6,
  problema7,
  problema8,
  problema9,
  problema10
];

const botones = document.querySelectorAll(".exercise-card a");

botones.forEach((boton, indice) => {
  boton.addEventListener("click", (evento) => {
    evento.preventDefault();
    problemas[indice]();
  });
});

aplicarModoGuardado();

function obtenerPanel(titulo) {
  let panel = document.querySelector(".exercise-workspace");

  if (!panel) {
    panel = document.createElement("section");
    panel.className = "exercise-workspace";
    panel.setAttribute("aria-live", "polite");
    document.querySelector(".page").appendChild(panel);
  }

  panel.innerHTML = `
    <header class="workspace-header">
      <h2>${titulo}</h2>
      <p>Ejercicio interactivo cargado desde JavaScript.</p>
    </header>
    <div class="workspace-body"></div>
  `;

  panel.scrollIntoView({ behavior: "smooth", block: "start" });
  return panel.querySelector(".workspace-body");
}

function problema1() {
  const cuerpo = obtenerPanel("Problema 1: Cambiador de Fondo Dinamico");
  cuerpo.innerHTML = `
    <p class="result-text">Presiona el boton para generar un color aleatorio.</p>
    <button class="action-button" type="button">Cambiar fondo</button>
  `;

  const resultado = cuerpo.querySelector(".result-text");
  const boton = cuerpo.querySelector(".action-button");

  boton.addEventListener("click", () => {
    const color = `#${Math.floor(Math.random() * 16777215).toString(16).padStart(6, "0")}`;
    document.body.style.backgroundColor = color;
    resultado.textContent = `Color aplicado: ${color}`;
  });
}

function problema2() {
  const cuerpo = obtenerPanel("Problema 2: Contador Incremental/Decremental");
  cuerpo.innerHTML = `
    <div class="counter-display">0</div>
    <div class="button-row">
      <button class="action-button" type="button" data-action="decrementar">Decrementar</button>
      <button class="action-button" type="button" data-action="resetear">Resetear</button>
      <button class="action-button" type="button" data-action="incrementar">Incrementar</button>
    </div>
  `;

  let contador = 0;
  const pantalla = cuerpo.querySelector(".counter-display");

  cuerpo.querySelectorAll("button").forEach((boton) => {
    boton.addEventListener("click", () => {
      if (boton.dataset.action === "incrementar") contador++;
      if (boton.dataset.action === "decrementar") contador--;
      if (boton.dataset.action === "resetear") contador = 0;
      pantalla.textContent = contador;
    });
  });
}

function problema3() {
  const cuerpo = obtenerPanel("Problema 3: Validacion de Formulario");
  cuerpo.innerHTML = `
    <label class="field-label" for="passwordInput">Contrasena</label>
    <input class="text-input password-input" id="passwordInput" type="password" placeholder="Minimo 8 caracteres">
    <p class="validation-message">Escribe una contrasena.</p>
  `;

  const input = cuerpo.querySelector("#passwordInput");
  const mensaje = cuerpo.querySelector(".validation-message");

  input.addEventListener("input", () => {
    const esValida = input.value.length >= 8;
    input.classList.toggle("is-valid", esValida);
    input.classList.toggle("is-invalid", !esValida);
    mensaje.textContent = esValida ? "Contrasena valida." : "Debe tener al menos 8 caracteres.";
    mensaje.classList.toggle("success", esValida);
  });
}

function problema4() {
  const tecnologias = ["JavaScript", "HTML", "CSS", "React", "Node.js", "Python", "Java", "PHP"];
  const cuerpo = obtenerPanel("Problema 4: Filtrado de Lista");
  cuerpo.innerHTML = `
    <input class="text-input" id="filterInput" type="text" placeholder="Buscar tecnologia">
    <ul class="simple-list">
      ${tecnologias.map((tecnologia) => `<li>${tecnologia}</li>`).join("")}
    </ul>
  `;

  const input = cuerpo.querySelector("#filterInput");
  const elementos = cuerpo.querySelectorAll(".simple-list li");

  input.addEventListener("input", () => {
    const busqueda = input.value.toLowerCase();
    elementos.forEach((elemento) => {
      const coincide = elemento.textContent.toLowerCase().includes(busqueda);
      elemento.style.display = coincide ? "block" : "none";
    });
  });
}

function problema5() {
  const imagenes = [
    crearImagenSvg("#009934", "Imagen 1"),
    crearImagenSvg("#0879df", "Imagen 2"),
    crearImagenSvg("#ff9f1c", "Imagen 3")
  ];
  const cuerpo = obtenerPanel("Problema 5: Galeria con Delegacion");
  cuerpo.innerHTML = `
    <div class="gallery-thumbs">
      ${imagenes.map((imagen, indice) => `<img src="${imagen}" data-src="${imagen}" alt="Miniatura ${indice + 1}">`).join("")}
    </div>
    <img class="main-image" src="${imagenes[0]}" alt="Imagen principal">
  `;

  const miniaturas = cuerpo.querySelector(".gallery-thumbs");
  const principal = cuerpo.querySelector(".main-image");

  miniaturas.addEventListener("click", (evento) => {
    if (evento.target.matches("img")) {
      principal.src = evento.target.dataset.src;
    }
  });
}

function problema6() {
  const usuarios = [
    { nombre: "Laura Gomez", rol: "Frontend", biografia: "Construye interfaces claras y accesibles." },
    { nombre: "Carlos Ruiz", rol: "Backend", biografia: "Disena servicios y APIs para aplicaciones web." },
    { nombre: "Marta Diaz", rol: "QA", biografia: "Prueba flujos y detecta errores antes de publicar." },
    { nombre: "Andres Mora", rol: "UX", biografia: "Convierte necesidades de usuarios en experiencias simples." }
  ];
  const cuerpo = obtenerPanel("Problema 6: Generador de Tarjetas");
  cuerpo.innerHTML = `<div class="user-card-grid"></div>`;

  const contenedor = cuerpo.querySelector(".user-card-grid");

  usuarios.forEach((usuario) => {
    const tarjeta = document.createElement("article");
    tarjeta.className = "user-card";
    tarjeta.innerHTML = `
      <h3>${usuario.nombre}</h3>
      <p class="user-role">${usuario.rol}</p>
      <p>${usuario.biografia}</p>
    `;
    contenedor.appendChild(tarjeta);
  });
}

function problema7() {
  const cuerpo = obtenerPanel("Problema 7: Modo Oscuro Persistente");
  cuerpo.innerHTML = `
    <p class="result-text">El modo elegido se guarda en localStorage.</p>
    <button class="action-button" type="button">Alternar modo oscuro</button>
  `;

  cuerpo.querySelector("button").addEventListener("click", () => {
    const activo = document.body.classList.toggle("dark-mode");
    localStorage.setItem("modoOscuro", activo ? "activo" : "inactivo");
  });
}

function problema8() {
  const productos = ["Computador", "Teclado", "Mouse", "Monitor", "Audifonos"];
  const cuerpo = obtenerPanel("Problema 8: Lista de Favoritos");
  cuerpo.innerHTML = `
    <ul class="product-list">
      ${productos.map((producto) => `
        <li data-producto="${producto}">
          <span>${producto}</span>
          <button class="small-button" type="button">Anadir a Favoritos</button>
        </li>
      `).join("")}
    </ul>
  `;

  const favoritos = JSON.parse(localStorage.getItem("favoritos") || "[]");
  const elementos = cuerpo.querySelectorAll(".product-list li");

  elementos.forEach((elemento) => {
    const producto = elemento.dataset.producto;
    if (favoritos.includes(producto)) elemento.classList.add("favorite");

    elemento.querySelector("button").addEventListener("click", () => {
      const guardados = JSON.parse(localStorage.getItem("favoritos") || "[]");
      if (!guardados.includes(producto)) guardados.push(producto);
      localStorage.setItem("favoritos", JSON.stringify(guardados));
      elemento.classList.add("favorite");
    });
  });
}

function problema9() {
  const cuerpo = obtenerPanel("Problema 9: Temporizador Regresivo");
  cuerpo.innerHTML = `
    <label class="field-label" for="secondsInput">Segundos</label>
    <input class="text-input" id="secondsInput" type="number" min="1" value="10">
    <button class="action-button" type="button">Iniciar</button>
    <div class="timer-display">10</div>
  `;

  const input = cuerpo.querySelector("#secondsInput");
  const boton = cuerpo.querySelector("button");
  const pantalla = cuerpo.querySelector(".timer-display");
  let intervalo = null;

  boton.addEventListener("click", () => {
    clearInterval(intervalo);
    let segundos = Number(input.value);
    pantalla.classList.remove("finished");

    if (!segundos || segundos < 1) {
      pantalla.textContent = "Ingresa un numero valido";
      return;
    }

    pantalla.textContent = segundos;
    intervalo = setInterval(() => {
      segundos--;
      pantalla.textContent = segundos;

      if (segundos <= 0) {
        clearInterval(intervalo);
        pantalla.textContent = "Tiempo cumplido!";
        pantalla.classList.add("finished");
      }
    }, 1000);
  });
}

function problema10() {
  const lenguajes = ["JavaScript", "Python", "Java", "C#", "PHP", "Ruby", "Swift", "Kotlin", "TypeScript", "Go"];
  const cuerpo = obtenerPanel("Problema 10: Buscador con Autocompletado");
  cuerpo.innerHTML = `
    <div class="autocomplete">
      <input class="text-input" id="autocompleteInput" type="text" placeholder="Buscar lenguaje">
      <ul class="suggestions"></ul>
    </div>
  `;

  const input = cuerpo.querySelector("#autocompleteInput");
  const lista = cuerpo.querySelector(".suggestions");

  input.addEventListener("input", () => {
    const busqueda = input.value.toLowerCase();
    lista.innerHTML = "";

    if (!busqueda) return;

    lenguajes
      .filter((lenguaje) => lenguaje.toLowerCase().includes(busqueda))
      .forEach((lenguaje) => {
        const item = document.createElement("li");
        item.textContent = lenguaje;
        item.addEventListener("click", () => {
          input.value = lenguaje;
          lista.innerHTML = "";
        });
        lista.appendChild(item);
      });
  });
}

function aplicarModoGuardado() {
  if (localStorage.getItem("modoOscuro") === "activo") {
    document.body.classList.add("dark-mode");
  }
}

function crearImagenSvg(color, texto) {
  const svg = `
    <svg xmlns="http://www.w3.org/2000/svg" width="720" height="420" viewBox="0 0 720 420">
      <rect width="720" height="420" fill="${color}"/>
      <circle cx="570" cy="88" r="64" fill="rgba(255,255,255,0.22)"/>
      <circle cx="112" cy="326" r="86" fill="rgba(255,255,255,0.18)"/>
      <text x="50%" y="52%" text-anchor="middle" font-family="Segoe UI, Arial, sans-serif" font-size="54" font-weight="800" fill="white">${texto}</text>
    </svg>
  `;

  return `data:image/svg+xml;charset=UTF-8,${encodeURIComponent(svg)}`;
}
