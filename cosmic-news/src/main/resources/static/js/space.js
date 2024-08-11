// Clase para representar planetas
class Planet {
    constructor(name, radius, col, orbitRadius = 0, orbitSpeed = 0, imgSrc = null) {
        this.name = name;
        this.radius = 2 * radius;
        this.col = col;
        this.orbitRadius = orbitRadius;
        this.orbitSpeed = 0.8 * orbitSpeed;
        this.orbitAngle = random(TWO_PI);
        this.orbitCenterX = 0;
        this.orbitCenterY = 0;
        this.imgSrc = imgSrc;
        this.img = null;
        this.x = 0;
        this.y = 0;
    }

    async loadImage() {
        if (this.imgSrc) {
            this.img = await loadImage(this.imgSrc); // Cargar la imagen desde Base64
        }
    }

    update() {
        this.orbitAngle += this.orbitSpeed;
        this.x = this.orbitCenterX + this.orbitRadius * cos(this.orbitAngle);
        this.y = this.orbitCenterY + this.orbitRadius * sin(this.orbitAngle);
    }

    updateMoon(x, y) {
        this.x = x + this.orbitRadius * cos(this.orbitAngle);
        this.y = y + this.orbitRadius * sin(this.orbitAngle);
        this.orbitAngle += this.orbitSpeed;
    }

    display() {
        if (this.img) {
            imageMode(CENTER);
            image(this.img, this.x, this.y, this.radius * 2, this.radius * 2);
        } else {
            fill(this.col);
            ellipse(this.x, this.y, this.radius * 2);
        }
    }

    isMouseOver(mouseX, mouseY) {
        let d = dist(mouseX, mouseY, this.x, this.y);
        return d < this.radius;
    }
}

let sun;
let planets = [];
let moon;
let zoom = 1; // Factor de zoom inicial
let offsetX = 0; // Desplazamiento en el eje X
let offsetY = 0; // Desplazamiento en el eje Y
let dragging = false; // Estado de arrastre
let lastMouseX, lastMouseY; // Posición del ratón en el último evento de arrastre
let selectedObject = null; // Objeto actualmente seleccionado para mostrar información
let canvasCenterX, canvasCenterY; // Centro del canvas

function preload() {
    // Cargar las imágenes del Sol desde un recurso local
    sunImage = loadImage('images/sun.png');
    // Cargar los planetas desde el backend
    loadPlanets();
}

async function loadPlanets() {
    try {
        const response = await fetch('/api/planets');
        const data = await response.json();

        const planetPromises = data.map(async planetData => {
            let imgSrc = null;
            if (planetData.imageUrl) {
                imgSrc = planetData.imageUrl; // URL Base64 de la imagen
            }

            return new Planet(
                planetData.name,
                planetData.radius,
                color(planetData.color),
                planetData.orbitRadius,
                planetData.orbitSpeed,
                imgSrc
            );
        });

        planets = await Promise.all(planetPromises);
        setupPlanets();
    } catch (error) {
        console.error('Error loading planets:', error);
    }
}

function setupPlanets() {
    canvasCenterX = width / 2;
    canvasCenterY = height / 2;

    // Configurar el Sol en el centro
    sun = new Planet('Sol', 50, color(255, 255, 0), 0, 0, sunImage);
    sun.orbitCenterX = canvasCenterX;
    sun.orbitCenterY = canvasCenterY;

    // Inicializar la Luna (como un planeta con órbita alrededor de la Tierra)
    moon = planets.find(p => p.name === 'Luna');
    if (moon) {
        moon.orbitCenterX = canvasCenterX;
        moon.orbitCenterY = canvasCenterY;
    }

    // Cargar imágenes de los planetas
    planets.forEach(planet => planet.loadImage());
}

function draw() {
    background(0);

    // Aplicar el zoom y desplazamiento
    translate(offsetX + canvasCenterX, offsetY + canvasCenterY);
    scale(zoom);
    translate(-canvasCenterX, -canvasCenterY);

    // Dibuja estrellas de fondo
    for (let star of stars) {
        fill(star.col);
        noStroke();
        ellipse(star.x, star.y, 2);
    }

    // Dibuja el Sol usando la imagen
    imageMode(CENTER);
    let sunX = canvasCenterX;
    let sunY = canvasCenterY;
    image(sunImage, sunX, sunY, sun.radius * 2, sun.radius * 2);

    // Dibuja y actualiza los planetas
    let hoveredObject = null;
    for (let planet of planets) {
        if (planet.name === 'Luna') {
            const earth = planets.find(p => p.name === 'La Tierra');
            if (earth) {
                planet.updateMoon(earth.x, earth.y);
            }
        } else {
            planet.update();
        }
        planet.display();

        // Detección de si el ratón está sobre un planeta o la Luna
        if (planet.isMouseOver(mouseX, mouseY)) {
            hoveredObject = planet;
        }

        if (planet.isMouseOver(mouseX, mouseY)) {
            fill(255);
            textAlign(CENTER);
            text(planet.name, planet.x, planet.y - planet.radius - 10);
        }
    }

    // Actualiza la información del contenedor si hay un objeto seleccionado
    if (selectedObject) {
        infoImage.attribute('src', selectedObject.imgSrc || '');
        infoName.html(selectedObject.name);
        infoContainer.style('display', 'block');
    } else {
        infoContainer.style('display', 'none');
    }
}

// Función para crear estrellas más allá del área visible
function createStars() {
    stars = []; // Limpiar las estrellas existentes
    let extraWidth = 2000; // Ancho adicional para generar estrellas fuera del canvas
    let extraHeight = 2000; // Alto adicional para generar estrellas fuera del canvas

    for (let i = -extraWidth / 2; i < width + extraWidth / 2; i += 20) {
        for (let j = -extraHeight / 2; j < height + extraHeight / 2; j += 20) {
            if (random() < 0.05) { // Controlar la densidad de las estrellas
                stars.push({ x: i + random(width), y: j + random(height), col: color(255, 255, 255) });
            }
        }
    }
}

// Función para manejar el zoom con la rueda del ratón
function mouseWheel(event) {
    zoom += event.deltaY * -0.001;
    zoom = constrain(zoom, 0.1, 10); // Limita el zoom para evitar valores extremos
    return false; // Evita el scroll de la página
}

// Función para manejar el arrastre del ratón
function mousePressed() {
    dragging = true;
    lastMouseX = mouseX;
    lastMouseY = mouseY;

    // Verificar si se hace clic en un objeto
    let adjustedMouseX = (mouseX - offsetX - canvasCenterX) / zoom + canvasCenterX;
    let adjustedMouseY = (mouseY - offsetY - canvasCenterY) / zoom + canvasCenterY;

    // Verificar clic en planetas
    selectedObject = isMouseOverPlanet(adjustedMouseX, adjustedMouseY);
}

function mouseDragged() {
    if (dragging) {
        let dx = mouseX - lastMouseX;
        let dy = mouseY - lastMouseY;
        offsetX += dx / zoom; // Ajustar el desplazamiento en función del zoom
        offsetY += dy / zoom;
        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }
}

function mouseReleased() {
    dragging = false;
}

// Ajusta el tamaño del canvas cuando se redimensiona la ventana
function windowResized() {
    resizeCanvas(windowWidth, windowHeight);
    canvasCenterX = width / 2;
    canvasCenterY = height / 2;
    // No se regeneran estrellas, pero se actualiza el centro de órbita
    for (let planet of planets) {
        planet.orbitCenterX = canvasCenterX;
        planet.orbitCenterY = canvasCenterY;
    }
}

// Función para detectar si el ratón está sobre algún planeta
function isMouseOverPlanet(mouseX, mouseY) {
    for (let planet of planets) {
        if (planet.isMouseOver(mouseX, mouseY)) {
            return planet;
        }
    }
    return null;
}