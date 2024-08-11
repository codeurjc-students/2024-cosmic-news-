// Clase para representar planetas
class Planet {
    constructor(radius, col, orbitRadius = 0, orbitSpeed = 0) {
        this.radius = radius;
        this.col = col;
        this.orbitRadius = orbitRadius;
        this.orbitSpeed = orbitSpeed;
        this.orbitAngle = random(TWO_PI);
        this.orbitCenterX = 0;
        this.orbitCenterY = 0;
    }

    update() {
        this.orbitAngle += this.orbitSpeed;
        this.x = this.orbitCenterX + this.orbitRadius * cos(this.orbitAngle);
        this.y = this.orbitCenterY + this.orbitRadius * sin(this.orbitAngle);
    }

    display() {
        fill(this.col);
        ellipse(this.x, this.y, this.radius * 2);
    }
}

// Clase para representar la Luna
class Moon {
    constructor(radius, col, orbitRadius = 0, orbitSpeed = 0) {
        this.radius = radius;
        this.col = col;
        this.orbitRadius = orbitRadius;
        this.orbitSpeed = orbitSpeed;
        this.orbitAngle = 0;
        this.x = 0;
        this.y = 0;
    }

    update(x, y) {
        this.x = x + this.orbitRadius * cos(this.orbitAngle);
        this.y = y + this.orbitRadius * sin(this.orbitAngle);
        this.orbitAngle += this.orbitSpeed;
    }

    display() {
        fill(this.col);
        ellipse(this.x, this.y, this.radius * 2);
    }
}

// Variables globales
let sun;
let sunImage;
let planets = [];
let stars = [];
let moon;
let zoom = 1; // Factor de zoom inicial
let offsetX = 0; // Desplazamiento en el eje X
let offsetY = 0; // Desplazamiento en el eje Y
let dragging = false; // Estado de arrastre
let lastMouseX, lastMouseY; // Posición del ratón en el último evento de arrastre

function preload() {
    // Cargar la imagen del Sol
    sunImage = loadImage('images/sun.png'); // Asegúrate de que la ruta sea correcta
}

function setup() {
    createCanvas(windowWidth, windowHeight);
    noStroke();

    // Crear estrellas de fondo
    for (let i = 0; i < 200; i++) {
        stars.push({ x: random(width), y: random(height), col: color(255, 255, 255) });
    }

    // Configurar el Sol en el centro
    sun = new Planet(50, color(255, 255, 0), 0, 0); // El color no se usará, ya que usaremos la imagen

    // Planetas
    planets.push(new Planet(4, color(169, 169, 169), 60, 0.01)); // Mercurio
    planets.push(new Planet(7, color(255, 255, 204), 100, 0.007)); // Venus
    planets.push(new Planet(8, color(0, 0, 255), 150, 0.005)); // Tierra
    planets.push(new Planet(7, color(255, 0, 0), 200, 0.004)); // Marte
    planets.push(new Planet(14, color(255, 204, 0), 300, 0.002)); // Júpiter
    planets.push(new Planet(12, color(255, 204, 100), 400, 0.0015)); // Saturno
    planets.push(new Planet(11, color(173, 216, 230), 500, 0.0012)); // Urano
    planets.push(new Planet(10, color(100, 149, 237), 600, 0.001)); // Neptuno

    // Establecer el centro de órbita de los planetas
    for (let planet of planets) {
        planet.orbitCenterX = width / 2;
        planet.orbitCenterY = height / 2;
    }

    // Luna (órbita alrededor de la Tierra)
    moon = new Moon(3, color(200, 200, 200), 30, 0.04); // Tamaño, color, radio de órbita y velocidad orbital
}

function draw() {
    background(0);

    // Aplicar el zoom y desplazamiento
    translate(width / 2 + offsetX, height / 2 + offsetY);
    scale(zoom);
    translate(-width / 2, -height / 2);

    // Dibuja estrellas de fondo
    for (let star of stars) {
        fill(star.col);
        noStroke();
        ellipse(star.x, star.y, 2);
    }

    // Dibuja el Sol usando la imagen
    imageMode(CENTER);
    image(sunImage, width / 2, height / 2, sun.radius * 2 * zoom, sun.radius * 2 * zoom);

    // Dibuja y actualiza los planetas
    for (let planet of planets) {
        planet.update();
        planet.display();
        
        // Actualiza la posición de la Luna (solo alrededor de la Tierra)
        if (planet.col.toString() === color(0, 0, 255).toString()) { // Tierra
            moon.update(planet.x, planet.y);
            moon.display();
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
}