class Planet {
    constructor(name, radius, orbitRadius = 0, orbitSpeed = 0, description, imgSrc = null) {
        this.name = name;
        this.radius = 2 * radius;
        this.orbitRadius = orbitRadius;
        this.orbitSpeed = 0.8 * orbitSpeed;
        this.orbitAngle = random(TWO_PI);
        this.orbitCenterX = 0;
        this.orbitCenterY = 0;
        this.description = description;
        this.imgSrc = imgSrc;
        this.img = null;
        this.x = 0;
        this.y = 0;
    }

    async loadImage() {
        if (this.imgSrc) {
            this.img = await loadImage(this.imgSrc);
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
let zoom = 1;
let offsetX = 0; 
let offsetY = 0; 
let dragging = false;
let lastMouseX, lastMouseY;
let selectedObject = null;
let canvasCenterX, canvasCenterY;

async function preload() {
    sunImage = loadImage('images/sun.png');
    await loadPlanets();
}

async function loadPlanets() {
    try {
        const response = await fetch('/api/planets');
        const data = await response.json();

        const planetPromises = data.map(async planetData => {

            return new Planet(
                planetData.name,
                planetData.radius,
                planetData.orbitRadius,
                planetData.orbitSpeed,
                planetData.description,
                planetData.imageUrl
            );
        });

        planets = await Promise.all(planetPromises);
        await setupPlanets();
    } catch (error) {
        console.error('Error loading planets:', error);
    }
}

async function setupPlanets() {
    createCanvas(windowWidth, windowHeight);
    noStroke();
    canvasCenterX = width / 2;
    canvasCenterY = height / 2;
    createStars();

    sun = new Planet('Sol', 50, 0, 0, 'images/sun.png');
    sun.orbitCenterX = canvasCenterX;
    sun.orbitCenterY = canvasCenterY;

    moon = planets.find(p => p.name === 'Luna');
    if (moon) {
        moon.orbitCenterX = canvasCenterX;
        moon.orbitCenterY = canvasCenterY;
    }

    for (let planet of planets) {
        planet.orbitCenterX = width / 2;
        planet.orbitCenterY = height / 2;
        planet.orbitCenterX = canvasCenterX;
        planet.orbitCenterY = canvasCenterY;
    }

    infoContainer = select('#info');
    infoImage = select('#info-image');
    infoDesc = select('#info-desc');

    infoContainer.style('display', 'none');

    let closeButton = select('#close-info');
    closeButton.mousePressed(() => {
        infoContainer.style('display', 'none');
    });

    await Promise.all(planets.map(planet => planet.loadImage()));
}

function setup() {
    preload();
}

function draw() {
    background(0);

    translate(offsetX + canvasCenterX, offsetY + canvasCenterY);
    scale(zoom);
    translate(-canvasCenterX, -canvasCenterY);

    for (let star of stars) {
        fill(star.col);
        noStroke();
        ellipse(star.x, star.y, 2);
    }

    imageMode(CENTER);
    let sunX = canvasCenterX;
    let sunY = canvasCenterY;
    image(sunImage, sunX, sunY, sun.radius * 2, sun.radius * 2);

    let adjustedMouseX = (mouseX - offsetX - canvasCenterX) / zoom + canvasCenterX;
    let adjustedMouseY = (mouseY - offsetY - canvasCenterY) / zoom + canvasCenterY;

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

        if (planet.isMouseOver(adjustedMouseX, adjustedMouseY)) {
            hoveredObject = planet;
            fill(255);
            textAlign(CENTER);
            if (planet.name === 'Sol'){
                text(planet.name, planet.x, planet.y - planet.radius + 20);
            }else{
                text(planet.name, planet.x, planet.y - planet.radius - 10);
            }
        }
    }

    if (selectedObject) {
        infoImage.attribute('src', selectedObject.imgSrc || '');
        infoDesc.html(selectedObject.description);
        infoContainer.style('display', 'block');
    } else {
        infoContainer.style('display', 'none');
    }
}

function createStars() {
    stars = [];
    let extraWidth = 2000; 
    let extraHeight = 2000; 

    for (let i = -extraWidth / 2; i < width + extraWidth / 2; i += 20) {
        for (let j = -extraHeight / 2; j < height + extraHeight / 2; j += 20) {
            if (random() < 0.05) { 
                stars.push({ x: i + random(width), y: j + random(height), col: color(255, 255, 255) });
            }
        }
    }
}


function mouseWheel(event) {

    if (mouseX >= select('#info').elt.offsetLeft && mouseX <= select('#info').elt.offsetLeft + select('#info').elt.offsetWidth) {
        return;
    }

    zoom += event.deltaY * -0.001;
    zoom = constrain(zoom, 0.1, 10);
    return false; 
}

function mousePressed() {
    dragging = true;
    lastMouseX = mouseX;
    lastMouseY = mouseY;

    let adjustedMouseX = (mouseX - offsetX - canvasCenterX) / zoom + canvasCenterX;
    let adjustedMouseY = (mouseY - offsetY - canvasCenterY) / zoom + canvasCenterY;

    selectedObject = isMouseOverPlanet(adjustedMouseX, adjustedMouseY);
}

function mouseDragged() {
    if (dragging) {
        let dx = mouseX - lastMouseX;
        let dy = mouseY - lastMouseY;
        offsetX += dx / zoom;
        offsetY += dy / zoom;
        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }
}

function mouseReleased() {
    dragging = false;
}

function windowResized() {
    resizeCanvas(windowWidth, windowHeight);
    canvasCenterX = width / 2;
    canvasCenterY = height / 2;
    for (let planet of planets) {
        planet.orbitCenterX = canvasCenterX;
        planet.orbitCenterY = canvasCenterY;
    }
}

function isMouseOverPlanet(mouseX, mouseY) {
    for (let planet of planets) {
        if (planet.isMouseOver(mouseX, mouseY)) {
            return planet;
        }
    }
    return null;
}