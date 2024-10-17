import { Component, ElementRef, OnInit } from '@angular/core';
import p5 from 'p5';
import { Planet } from '../../models/planet.model';
import { PlanetService } from '../../services/planet.service';

@Component({
  selector: 'space',
  templateUrl: './space.component.html',
  styleUrls: ['../../styles/space.css']
})
export class SpaceComponent implements OnInit{
  private planets: Planet[] = [];
  private p5Instance: p5;
  private sunImage: p5.Image | undefined;
  private stars: { x: number, y: number, size: number, color: p5.Color }[] = [];

  constructor(
    private elementRef: ElementRef,
    private planetService: PlanetService
  ) {}

  ngOnInit(): void {
    this.hidePlanetInfo();
    this.planetService.getPlanets().subscribe(
      (planets) => {
        this.planets = planets;
        this.loadPlanetImages(); 
      },
      (error) => {
        console.error('Error fetching planets:', error);
      }
    );
  }

  private loadPlanetImages(): void {
    let loadedImages = 0;
    let totalImages = this.planets.length;

    this.p5Instance = new p5((p: p5) => {
      for (let planet of this.planets) {
        this.planetService.getPlanetImage(planet).subscribe(
          (response) => {
            if (response) {
              const blob = new Blob([response], { type: 'image/jpeg' });
              const imageUrl = URL.createObjectURL(blob);
              p.loadImage(imageUrl, (img) => {
                if (planet.id === 9) {
                  this.sunImage = img;
                }
                planet.imageUrl = imageUrl;
                loadedImages++;

                if (loadedImages === totalImages) {
                  this.startSketch(p);
                }
              });
            } else {
              loadedImages++;
              if (loadedImages === totalImages) {
                this.startSketch(p);
              }
            }
          },
          (error) => {
            console.error(`Error loading image for planet ${planet.name}:`, error);
            loadedImages++;
            if (loadedImages === totalImages) {
              this.startSketch(p);
            }
          }
        );
      }
    });
  }

  private startSketch(p: p5): void {
    const sketch = (p: p5) => {
      let planetDisplays: PlanetDisplay[] = [];
      let earthDisplay: PlanetDisplay | undefined;

      p.setup = () => {
        const canvas = p.createCanvas(window.innerWidth, window.innerHeight);
        canvas.parent(this.elementRef.nativeElement.querySelector('#canvas-container'));

        const unwantedCanvas = document.querySelector('main > canvas');
        if (unwantedCanvas) {
            unwantedCanvas.remove();
        }

        this.createStars(p, canvas.width, canvas.height);

        for (let planet of this.planets) {
          const planetDisplay = new PlanetDisplay(planet, p);
          planetDisplays.push(planetDisplay);

          if (planet.id === 3) {
            earthDisplay = planetDisplay;
          }
        }
      };

      p.draw = () => {
        p.background(0);
      
        for (const star of this.stars) {
          p.fill(star.color);
          p.noStroke();
          p.ellipse(star.x, star.y, star.size);
        }
      
        if (this.sunImage) {
          p.imageMode(p.CENTER);
          p.image(this.sunImage, window.innerWidth / 2, window.innerHeight / 2, 100, 100);
        }
      
        for (let planetDisplay of planetDisplays) {
          const centerX = planetDisplay.data.id === 10 && earthDisplay 
                          ? earthDisplay.x 
                          : window.innerWidth / 2;
          const centerY = planetDisplay.data.id === 10 && earthDisplay 
                          ? earthDisplay.y 
                          : window.innerHeight / 2;
      
          planetDisplay.update(centerX, centerY);
          planetDisplay.display();
      
          if (planetDisplay.isHovered(p.mouseX, p.mouseY)) {
            if (planetDisplay.data.name === undefined || planetDisplay.data.radius === undefined) {
              return;
            }
            p.fill(255);
            p.textSize(16);
            p.textAlign(p.CENTER, p.BOTTOM);
            if (planetDisplay.data.name==="Sol"){
              p.text(planetDisplay.data.name, planetDisplay.x, planetDisplay.y - planetDisplay.data.radius + 25); 
            }else{
              p.text(planetDisplay.data.name, planetDisplay.x, planetDisplay.y - planetDisplay.data.radius - 10);
            }
          }
        }
      };
      
      p.mousePressed = () => {
        for (let planetDisplay of planetDisplays) {
          if (planetDisplay.isClicked(p.mouseX, p.mouseY)) {
            this.showPlanetInfo(planetDisplay.data);
            break;
          }
        }
      };

      p.windowResized = () => {
        p.resizeCanvas(window.innerWidth, window.innerHeight);
        this.createStars(p, p.width, p.height);
      };
    };

    this.p5Instance = new p5(sketch);
  }

  private createStars(p: p5, width: number, height: number): void {
    this.stars = [];
    const starCount = Math.floor((width * height) / 5000); 

    for (let i = 0; i < starCount; i++) {
      const star = {
        x: p.random(width),
        y: p.random(height),
        size: p.random(1, 3), 
        color: p.color(255)
      };
      this.stars.push(star);
    }
  }

  private showPlanetInfo(planet: Planet): void {
    const infoContainer = document.getElementById('info-desc');
    const infoPanel = document.getElementById('info');
  
    if (infoContainer) {
      infoContainer.innerHTML = `
        <h2>${planet.name}</h2>
        <img src="${planet.imageUrl}" alt="${planet.name}" width="100">
        <p>${planet.description}</p>
      `;
    }
  
    if (infoPanel) {
      infoPanel.style.display = 'block';
    }
  }

  hidePlanetInfo(): void {
    const infoPanel = document.getElementById('info');
    if (infoPanel) {
      infoPanel.style.display = 'none';
    }
  }
}

class PlanetDisplay {
  x: number = 0;
  y: number = 0;
  orbitAngle: number;
  img: p5.Image | null = null;

  constructor(
    public data: Planet,
    private p5: p5
  ) {
    this.data.radius = (this.data.radius || 1) * 2;
    this.data.orbitSpeed = (this.data.orbitSpeed || 1) * 0.8;
    this.orbitAngle = this.p5.random(this.p5.TWO_PI);

    if (this.data.imageUrl) {
      this.img = this.p5.loadImage(this.data.imageUrl);
    }
  }

  update(orbitCenterX: number, orbitCenterY: number) {
    this.orbitAngle += this.data.orbitSpeed || 0;
    this.x = orbitCenterX + (this.data.orbitRadius || 0) * Math.cos(this.orbitAngle);
    this.y = orbitCenterY + (this.data.orbitRadius || 0) * Math.sin(this.orbitAngle);
  }

  display() {
    if (this.data.radius === undefined) {
      return;
    }
    if (this.img) {
      this.p5.imageMode(this.p5.CENTER);
      this.p5.image(this.img, this.x, this.y, this.data.radius * 2, this.data.radius * 2);
    } else {
      this.p5.ellipse(this.x, this.y, this.data.radius * 2);
    }
  }

  isClicked(mouseX: number, mouseY: number): boolean {
    if (this.data.radius === undefined) {
      return false;
    }
    const distance = this.p5.dist(mouseX, mouseY, this.x, this.y);
    return distance < this.data.radius;
  }

  isHovered(mouseX: number, mouseY: number): boolean {
    if (this.data.radius === undefined) {
      return false; 
    }
    const distance = this.p5.dist(mouseX, mouseY, this.x, this.y);
    return distance < this.data.radius;
  }
}