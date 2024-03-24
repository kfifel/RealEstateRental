# Stage 1: Build the Angular application
FROM node:14 AS build
WORKDIR /app

# Copy package.json and package-lock.json to install dependencies
COPY package*.json ./

# Install dependencies
RUN npm install -force

# Copy the rest of the application code
COPY . .

# Build the application for production
RUN npm run build-prod

# Stage 2: Serve the application from Nginx
FROM nginx:alpine
# Copy the built application from the previous stage
COPY --from=build /app/dist/front-angular /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Start Nginx and serve the application
CMD ["nginx", "-g", "daemon off;"]
