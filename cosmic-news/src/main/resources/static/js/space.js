// Clase para representar planetas
class Planet {
    constructor(name, radius, col, orbitRadius = 0, orbitSpeed = 0, img = null) {
        this.name = name;
        this.radius = 2 * radius;
        this.col = col;
        this.orbitRadius = orbitRadius;
        this.orbitSpeed = 0.8 * orbitSpeed;
        this.orbitAngle = random(TWO_PI);
        this.orbitCenterX = 0;
        this.orbitCenterY = 0;
        this.img = img;
        this.x = 0;
        this.y = 0;
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
let sunImage;
let earthImage;
let mercuryImage;
let venusImage;
let marsImage;
let jupiterImage;
let saturnImage;
let uranusImage;
let neptuneImage;
let moonImage;
let planets = [];
let stars = [];
let moon;
let zoom = 1; // Factor de zoom inicial
let offsetX = 0; // Desplazamiento en el eje X
let offsetY = 0; // Desplazamiento en el eje Y
let dragging = false; // Estado de arrastre
let lastMouseX, lastMouseY; // Posición del ratón en el último evento de arrastre
let selectedObject = null; // Objeto actualmente seleccionado para mostrar información
let canvasCenterX, canvasCenterY; // Centro del canvas

function preload() {
    // Cargar las imágenes del Sol y los planetas
    sunImage = loadImage('images/sun.png');
    earthImage = loadImage('images/earth.png');
    mercuryImage = loadImage('images/mercury.png');
    venusImage = loadImage('images/venus.png');
    marsImage = loadImage('images/mars.png');
    jupiterImage = loadImage('images/jupiter.png');
    saturnImage = loadImage('images/saturn.png');
    uranusImage = loadImage('images/uranus.png');
    neptuneImage = loadImage('images/neptune.png');
    moonImage = loadImage('images/moon.png');
}

function setup() {
    createCanvas(windowWidth, windowHeight);
    noStroke();

    // Inicializar el centro del canvas
    canvasCenterX = width / 2;
    canvasCenterY = height / 2;

    // Crear estrellas de fondo y almacenar sus posiciones
    createStars();

    // Configurar el Sol en el centro
    sun = new Planet('Sol', 50, color(255, 255, 0), 0, 0, sunImage);

    // Planetas
    planets.push(new Planet('Mercurio', 4, color(169, 169, 169), 110, 0.01, mercuryImage));
    planets.push(new Planet('Venus', 7, color(255, 255, 204), 170, 0.007, venusImage));
    planets.push(new Planet('La Tierra', 8, color(0, 0, 255), 240, 0.005, earthImage));
    planets.push(new Planet('Marte', 7, color(255, 0, 0), 320, 0.004, marsImage));
    planets.push(new Planet('Jupiter', 14, color(255, 204, 0), 400, 0.002, jupiterImage));
    planets.push(new Planet('Saturno', 12, color(255, 204, 100), 500, 0.0015, saturnImage));
    planets.push(new Planet('Urano', 11, color(173, 216, 230), 600, 0.0012, uranusImage));
    planets.push(new Planet('Neptuno', 10, color(100, 149, 237), 700, 0.001, neptuneImage));
    planets.push(sun);

    // Establecer el centro de órbita de los planetas
    for (let planet of planets) {
        planet.orbitCenterX = canvasCenterX;
        planet.orbitCenterY = canvasCenterY;
    }

    // Luna (órbita alrededor de la Tierra)
    moon = new Planet('Luna', 3, color(200, 200, 200), 30, 0.04, moonImage);
    planets.push(moon);

    // Inicializar el contenedor de información
    infoContainer = select('#info');
    infoImage = select('#info-image');
    infoName = select('#info-name');

    // Inicialmente ocultar el contenedor de información
    infoContainer.style('display', 'none');

    // Añadir evento para el botón de cerrar
    let closeButton = select('#close-info');
    closeButton.mousePressed(() => {
        infoContainer.style('display', 'none');
    });
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

    // Ajustar las coordenadas del ratón para la detección
    let adjustedMouseX = (mouseX - offsetX - canvasCenterX) / zoom + canvasCenterX;
    let adjustedMouseY = (mouseY - offsetY - canvasCenterY) / zoom + canvasCenterY;

    // Dibuja el Sol usando la imagen
    imageMode(CENTER);
    let sunX = canvasCenterX;
    let sunY = canvasCenterY;
    image(sunImage, sunX, sunY, sun.radius * 2, sun.radius * 2);

    // Dibuja y actualiza los planetas
    let hoveredObject = null;
    for (let planet of planets) {
        if(planet.name===("Luna")){
            for (let p of planets){
                if (p.name===("La Tierra")){
                    planet.updateMoon(p.x,p.y);
                }
            }
        }else{
            planet.update();
        }
        planet.display();
        
        // Detección de si el ratón está sobre un planeta o la Luna
        if (planet.isMouseOver(adjustedMouseX, adjustedMouseY)) {
            hoveredObject = planet;
        }

        if (planet.isMouseOver(adjustedMouseX, adjustedMouseY)) {
            fill(255);
            textAlign(CENTER);
            if(planet.name === ("Sol")){
                text(planet.name, planet.x, planet.y - planet.radius + 25);
            }else{
                text(planet.name, planet.x, planet.y - planet.radius - 10);
            }
        }
    }

    // Actualiza la información del contenedor si hay un objeto seleccionado
    if (selectedObject) {
        infoImage.attribute('src', selectedObject.img?.src || '');
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